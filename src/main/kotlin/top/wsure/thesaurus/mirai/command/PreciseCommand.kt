package top.wsure.thesaurus.mirai.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.MessageChain
import top.wsure.thesaurus.PluginMain
import top.wsure.thesaurus.mirai.utils.ThesaurusUtils
import top.wsure.thesaurus.ws.thesaurus.enums.MessageType

/**
 * FileName: Add
 * Author:   wsure
 * Date:     2021/4/12
 * Description:
 */
@ConsoleExperimentalApi
object PreciseCommand : RawCommand(
    PluginMain, "精确问", "精确+", "+精确问", "加精确问",
    description = "词库添加问题", usage = "精确问 <关键词>    #添加精确问的问题"
) {

    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    override suspend fun CommandSender.onCommand(args: MessageChain) {
        ThesaurusUtils.setAddQACacheOption(this, args, MessageType.PRECISE)
    }

}