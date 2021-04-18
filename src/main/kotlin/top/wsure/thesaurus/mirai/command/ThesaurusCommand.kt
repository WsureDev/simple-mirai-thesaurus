package top.wsure.thesaurus.mirai.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.MessageChain
import top.wsure.thesaurus.PluginMain
import top.wsure.thesaurus.mirai.data.Constant.GLOBAL_TAG
import top.wsure.thesaurus.mirai.data.Constant.GROUPS_SETTINGS
import top.wsure.thesaurus.mirai.data.Constant.THESAURUS_SERVICE
import top.wsure.thesaurus.mirai.utils.ThesaurusUtils
import top.wsure.thesaurus.ws.thesaurus.data.Word
import top.wsure.thesaurus.ws.thesaurus.enums.MessageType

object ThesaurusCommand: CompositeCommand(
    PluginMain,
    "词库",
    "词库管理",
    description = "词库管理操作"
) {
    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional = true

    @SubCommand("开启","open","enable","开")
    suspend fun CommandSender.open(){
        setEnableStatus(this,true)
    }

    @SubCommand("关闭","close","disable","关")
    suspend fun CommandSender.close(){
        setEnableStatus(this,false)
    }

    @SubCommand("删词","del","remove","删")
    suspend fun CommandSender.del(args:MessageChain){
        ThesaurusUtils.auth(this,{member->
            val precise = THESAURUS_SERVICE.delThesaurus(member.group.id, Word(args.serializeToMiraiCode(),MessageType.PRECISE))
            val fuzzy = THESAURUS_SERVICE.delThesaurus(member.group.id, Word(args.serializeToMiraiCode(),MessageType.FUZZY))
            ThesaurusUtils.createGroupThesaurusCache(member.group.id)
            sendMessage("删除精确问${precise}个,删除模糊问:${fuzzy}个")
        },{
            val precise = THESAURUS_SERVICE.delThesaurus(GLOBAL_TAG, Word(args.serializeToMiraiCode(),MessageType.PRECISE))
            val fuzzy = THESAURUS_SERVICE.delThesaurus(GLOBAL_TAG, Word(args.serializeToMiraiCode(),MessageType.FUZZY))
            ThesaurusUtils.createGlobalThesaurusCache()
            sendMessage("删除精确问${precise}个,删除模糊问:${fuzzy}个")
        },{})
    }

    private suspend fun setEnableStatus(sender: CommandSender, enable:Boolean){
        ThesaurusUtils.auth(sender,{ member->
            THESAURUS_SERVICE.setGroupSetting(member.group.id,enable)
            val setting = GROUPS_SETTINGS[member.group.id]
            if(setting!=null ){
                GROUPS_SETTINGS[member.group.id] = Pair(enable,setting.second)
            }
            sender.sendMessage("本群已${ if(enable) "开启" else "关闭"}词库")
        },{
            THESAURUS_SERVICE.setGroupSetting(GLOBAL_TAG,enable)
            val setting = GROUPS_SETTINGS[GLOBAL_TAG]
            if(setting!=null ){
                GROUPS_SETTINGS[GLOBAL_TAG] = Pair(enable,setting.second)
            }
            sender.sendMessage("全局词库已${ if(enable) "开启" else "关闭"}")
        },{})
    }
}