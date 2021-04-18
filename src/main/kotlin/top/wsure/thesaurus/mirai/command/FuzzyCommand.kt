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
object FuzzyCommand:RawCommand(
    PluginMain,"模糊问","模糊+","+模糊问","加模糊问",
    description = "词库添加问题",usage = "模糊问 <关键词>    #添加模糊问的问题"
) {

    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    override suspend fun CommandSender.onCommand(args: MessageChain) {
        ThesaurusUtils.setAddQACacheOption(this,args,MessageType.FUZZY)
    }

}