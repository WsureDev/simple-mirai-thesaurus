package top.wsure.thesaurus

import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info
import top.wsure.thesaurus.mirai.command.FuzzyCommand
import top.wsure.thesaurus.mirai.command.PreciseCommand
import top.wsure.thesaurus.mirai.data.Constant
import top.wsure.thesaurus.mirai.utils.ThesaurusUtils
import top.wsure.thesaurus.ws.thesaurus.utils.DBUtils
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AhoCorasickMatcher

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "top.wsure.thesaurus",
        name = "simple-mirai-thesaurus",
        version = "0.0.1"
    ) {
        author("WsureDev")
        info("""
            简易词库插件.
        """.trimIndent())
    }
) {

    override fun onEnable() {
        showDeviceInfo()
        PluginMain.launch {
            initDatabase()
        }
        FuzzyCommand.register()
        PreciseCommand.register()
        globalEventChannel().subscribeAlways<MessageEvent> {
            ThesaurusUtils.replayMessageHandler(it)
            ThesaurusUtils.addQAHandler(it)
        }

        logger.info { "Plugin loaded" }
    }
}

fun showDeviceInfo(){
    val osName = System.getProperty("os.name").split(" ")[0]
    val osArch = System.getProperty("os.arch")
    PluginMain.logger.info{"os.name:${osName}"}
    PluginMain.logger.info{"os.arch:${osArch}"}
    PluginMain.logger.info{"os.version:${System.getProperty("os.version")}"}
    val ac = AhoCorasickMatcher<String>{it}.match("a!bccab!!", arrayListOf("a","ab","bab","bc","bca","c","caa"))
    PluginMain.logger.info{Json.encodeToString(ac) }

}

fun initDatabase(){
    val db = DBUtils.getDatabase(Constant.databaseFile)
    DBUtils.initTableIfNotExist(db)
}