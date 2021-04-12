package top.wsure.thesaurus.mirai.utils

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.events.MessageEvent
import top.wsure.thesaurus.mirai.data.Constant
import top.wsure.thesaurus.mirai.data.Constant.GLOBAL_KEY
import top.wsure.thesaurus.mirai.data.Constant.GROUPS_SETTINGS
import top.wsure.thesaurus.mirai.data.Constant.THESAURUS_SERVICE
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AcNode

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
            cache[GLOBAL_KEY] = THESAURUS_SERVICE.globalCache()
            Bot.instances.flatMap { it.groups }.forEach {
                cache["${it.id}::"] = THESAURUS_SERVICE.groupsCache(it.id)
            }
            return cache
        }

        fun handleMessageEvent(event: MessageEvent) {
            when (event.subject) {
                is Group -> {
                    val groupId = event.subject.id
                    val settings = GROUPS_SETTINGS[groupId]
                    if (settings == null) {
                        //todo initGroupSetting
                    } else {
                        if (settings.first){
                            handleGroupEvent(event, groupId)
                            handleGlobalEvent(event)
                        }
                    }
                }
                is Member -> {
                    handleGroupEvent(event, (event.subject as Member).group.id)
                    handleGlobalEvent(event)
                }
                is Friend,
                is Stranger,
                is OtherClient -> {
                    handleGlobalEvent(event)
                }
            }
        }

        fun enabled(groupId: Long):Boolean{
            val settings = GROUPS_SETTINGS[groupId]
            return if (settings == null) {
                THESAURUS_SERVICE.initGroupSetting(groupId)
                GROUPS_SETTINGS[groupId] = Pair(false,1)
                false
            } else {
                settings.first
            }
        }

        fun handleGlobalEvent(event: MessageEvent) {

        }

        fun handleGroupEvent(event: MessageEvent, groupId: Long) {

        }
    }
}