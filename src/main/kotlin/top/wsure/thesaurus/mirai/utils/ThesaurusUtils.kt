package top.wsure.thesaurus.mirai.utils

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChain.Companion.deserializeJsonToMessageChain
import top.wsure.thesaurus.mirai.data.Constant.THESAURUS_AC_CACHE
import top.wsure.thesaurus.mirai.data.Constant.GLOBAL_TAG
import top.wsure.thesaurus.mirai.data.Constant.GROUPS_SETTINGS
import top.wsure.thesaurus.mirai.data.Constant.THESAURUS_SERVICE
import top.wsure.thesaurus.ws.thesaurus.data.Word
import top.wsure.thesaurus.ws.thesaurus.enums.MessageType
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AcNode
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AhoCorasickMatcher

/**
 * FileName: ThesaurusUtils
 * Author:   wsure
 * Date:     2021/4/7 9:26 上午
 * Description:
 */
class ThesaurusUtils {
    companion object {
        fun createThesaurusCache(): MutableMap<String, AcNode<String>> {
            val cache = HashMap<String, AcNode<String>>()
            cache["${GLOBAL_TAG}::"] = THESAURUS_SERVICE.globalCache()
            Bot.instances.flatMap { it.groups }.forEach {
                cache["${it.id}::"] = THESAURUS_SERVICE.groupsCache(it.id)
            }
            return cache
        }

        fun createGroupThesaurusCache(groupId: Long){
            THESAURUS_AC_CACHE["${groupId}::"] = THESAURUS_SERVICE.groupsCache(groupId)
        }
        fun createGlobalThesaurusCache(){
            THESAURUS_AC_CACHE["${GLOBAL_TAG}::"] = THESAURUS_SERVICE.globalCache()
        }

        suspend fun handleMessageEvent(event: MessageEvent) {
            when (event.subject) {
                is Group -> {
                    val groupId = event.subject.id
                    if (enabled(groupId)) {
                        sendMessage(event, groupId)
                        sendMessage(event, GLOBAL_TAG)
                    }
                }
                is Member -> {
                    val groupId = (event.subject as Member).group.id
                    if(enabled(groupId)) {
                        sendMessage(event, groupId)
                    }
                    sendMessage(event, GLOBAL_TAG)
                }
                is Friend,
                is Stranger,
                is OtherClient -> {
                    sendMessage(event, GLOBAL_TAG)
                }
            }
        }

        fun enabled(groupId: Long): Boolean {
            val settings = GROUPS_SETTINGS[groupId]
            return if (settings == null) {
                THESAURUS_SERVICE.initGroupSetting(groupId)
                GROUPS_SETTINGS[groupId] = Pair(false, 1)
                false
            } else {
                settings.first
            }
        }

        private suspend fun sendMessage(event: MessageEvent, groupId: Long) {
            val msg = event.message.serializeToMiraiCode()
            val words = THESAURUS_SERVICE.queryThesaurus(groupId, Word(msg,MessageType.PRECISE))
            if(words.isNotEmpty()){
                words.forEach {
                    event.subject.sendMessage(it.answer.deserializeJsonToMessageChain())
                }
            } else {
                val ac = AhoCorasickMatcher<String>{it}
                val cache = THESAURUS_AC_CACHE["${groupId}::"]
                if(cache != null){
                    ac.match(msg,cache)
                        .map { THESAURUS_SERVICE.queryThesaurus(groupId, Word(msg,MessageType.FUZZY)) }
                        .flatten()
                        .forEach {
                            event.subject.sendMessage(it.answer.deserializeJsonToMessageChain())
                        }
                }
            }
        }
    }
}