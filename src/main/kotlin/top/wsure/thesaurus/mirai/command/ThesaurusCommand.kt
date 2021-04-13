package top.wsure.thesaurus.mirai.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.MessageChain
import top.wsure.thesaurus.PluginMain
import top.wsure.thesaurus.mirai.data.Constant.GROUPS_SETTINGS
import top.wsure.thesaurus.mirai.data.Constant.THESAURUS_OPTION_CACHE
import top.wsure.thesaurus.mirai.utils.ThesaurusUtils
import top.wsure.thesaurus.ws.thesaurus.data.Word
import top.wsure.thesaurus.ws.thesaurus.enums.MessageType

/**
 * FileName: Add
 * Author:   wsure
 * Date:     2021/4/12
 * Description: 
 */
object ThesaurusCommand:RawCommand(
    PluginMain,"精确问","精确+","+精确问","加精确问",
    description = "词库添加问题"
) {

    override suspend fun CommandSender.onCommand(args: MessageChain) {
        when(this.subject){
            is Group ,
            is Member -> {
                val member = this.user as Member
                ThesaurusUtils.enabled(member.group.id)
                if(member.permission.level >= GROUPS_SETTINGS[member.group.id]?.second ?: 1){
                    THESAURUS_OPTION_CACHE["group::${member.group.id}::${member.id}"] =
                        Word(args.serializeToMiraiCode(),System.currentTimeMillis().toString(),MessageType.PRECISE)
                    sendMessage("请发送精确问的回答，3分钟内有效")
                }
            }
        }
    }

}