package top.wsure.thesaurus.mirai.utils

import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.isUser
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.MessageChain
import top.wsure.thesaurus.mirai.data.Constant
import top.wsure.thesaurus.mirai.data.Constant.GLOBAL_TAG
import top.wsure.thesaurus.mirai.data.Constant.GROUPS_SETTINGS
import top.wsure.thesaurus.mirai.data.Constant.THESAURUS_AC_CACHE
import top.wsure.thesaurus.mirai.data.Constant.THESAURUS_OPTION_CACHE
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

        fun createGroupThesaurusCache(groupId: Long) {
            THESAURUS_AC_CACHE["${groupId}::"] = THESAURUS_SERVICE.groupsCache(groupId)
        }

        fun createGlobalThesaurusCache() {
            THESAURUS_AC_CACHE["${GLOBAL_TAG}::"] = THESAURUS_SERVICE.globalCache()
        }

        suspend fun replayMessageHandler(event: MessageEvent) {
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
                    if (enabled(groupId)) {
                        sendMessage(event, groupId)
                    }
                    if (enabled(GLOBAL_TAG)) {
                        sendMessage(event, GLOBAL_TAG)
                    }
                }
                is Friend,
                is Stranger,
                is OtherClient -> {
                    if (enabled(GLOBAL_TAG)) {
                        sendMessage(event, GLOBAL_TAG)
                    }
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
            val words = THESAURUS_SERVICE.queryThesaurus(groupId, Word(msg, MessageType.PRECISE))
            if (words.isNotEmpty()) {
                words.forEach {
                    event.subject.sendMessage(it.answer.deserializeMiraiCode())
                }
            } else {
                val ac = AhoCorasickMatcher<String> { it }
                val cache = THESAURUS_AC_CACHE["${groupId}::"]
                if (cache != null) {
                    ac.match(msg, cache)
                        .map { res -> THESAURUS_SERVICE.queryThesaurus(groupId, Word(res.pattern, MessageType.FUZZY)) }
                        .flatten()
                        .forEach {
                            event.subject.sendMessage(it.answer.deserializeMiraiCode())
                        }
                }
            }
        }

        fun setQACache(args: MessageChain, messageType: MessageType, cacheKey: String, timeout: Long) {
            clearTimeOut()
            THESAURUS_OPTION_CACHE[cacheKey] =
                Word(args.serializeToMiraiCode(), (System.currentTimeMillis() + timeout).toString(), messageType)
        }

        fun getQACache(cacheKey: String): Word? {
            clearTimeOut()
            return THESAURUS_OPTION_CACHE[cacheKey]
        }

        fun removeQACache(cacheKey: String): Word? {
            clearTimeOut()
            return THESAURUS_OPTION_CACHE.remove(cacheKey)
        }

        private fun clearTimeOut() {
            val now = System.currentTimeMillis()
            THESAURUS_OPTION_CACHE.keys.stream()
                .filter { THESAURUS_OPTION_CACHE[it] != null && THESAURUS_OPTION_CACHE[it]!!.answer != "0" && now.toString() > THESAURUS_OPTION_CACHE[it]!!.answer }
                .forEach { THESAURUS_OPTION_CACHE.remove(it) }
        }

        fun groupQAKey(member: Member): String {
            return "group::${member.group.id}::${member.id}"
        }

        fun globalQAKey(userId: Long): String {
            return "global::${userId}::"
        }

        suspend fun setAddQACacheOption(sender: CommandSender, chain: MessageChain, qaType: MessageType) {
            auth(sender, { member ->
                setQACache(chain, qaType, groupQAKey(member), 3 * 60000)
                sender.sendMessage("请发送${qaType.desc}回答，3分钟内有效")
            }, { user ->
                setQACache(chain, qaType, globalQAKey(user.id), 3 * 60000)
                sender.sendMessage("请发送${qaType.desc}的回答，3分钟内有效")
            }, {})
        }

        suspend fun addQAHandler(event: MessageEvent) {
            when (event.subject) {
                is Group,
                is Member -> {
                    val member = event.sender as Member
                    val qaCache = getQACache(groupQAKey(member))
                    if (qaCache != null) {
                        qaCache.answer = event.message.serializeToMiraiCode()
                        THESAURUS_SERVICE.addThesaurus(member.group.id, qaCache, member.id)
                        removeQACache(groupQAKey(member))
                        createGroupThesaurusCache(member.group.id)
                        event.subject.sendMessage("回复已记录")
                    }
                }
                is Friend,
                is Stranger,
                is OtherClient -> {
                    val qaCache = getQACache(globalQAKey(event.sender.id))
                    if (qaCache != null) {
                        qaCache.answer = event.message.serializeToMiraiCode()
                        THESAURUS_SERVICE.addThesaurus(GLOBAL_TAG, qaCache, event.sender.id)
                        removeQACache(globalQAKey(event.sender.id))
                        createGlobalThesaurusCache()
                        event.subject.sendMessage("回复已记录")
                    }
                }
            }
        }

        suspend fun auth(
            sender: CommandSender,
            groupFun: suspend (member: Member) -> Unit,
            globalFun: suspend (user: User) -> Unit,
            consoleFun: suspend () -> Unit
        ) {
            if (sender.isUser()) {
                when (sender.subject) {
                    is Group,
                    is Member -> {
                        val member = sender.user as Member
                        enabled(member.group.id)
                        if (member.permission.level >= GROUPS_SETTINGS[member.group.id]?.second ?: 1) {
                            groupFun(member)
                        }
                    }
                    is Friend,
                    is Stranger,
                    is OtherClient -> {
                        if (sender.hasPermission(Constant.GLOBAL_EDITOR_PERMISSION)) {
                            globalFun(sender.user)
                        }
                    }
                }
            } else {
                consoleFun()
            }
        }
    }
}