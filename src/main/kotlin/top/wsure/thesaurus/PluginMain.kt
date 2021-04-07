package top.wsure.thesaurus

import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import top.wsure.thesaurus.mirai.data.Constant
import top.wsure.thesaurus.ws.thesaurus.utils.DBUtils
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AhoCorasickMatcherCopy

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

        logger.info { "Plugin loaded" }
    }
}

fun showDeviceInfo(){
    val osName = System.getProperty("os.name").split(" ")[0]
    val osArch = System.getProperty("os.arch")
    PluginMain.logger.info{"os.name:${osName}"}
    PluginMain.logger.info{"os.arch:${osArch}"}
    PluginMain.logger.info{"os.version:${System.getProperty("os.version")}"}
    PluginMain.logger.info{Json.encodeToString(AhoCorasickMatcherCopy().match("asdasdkasklzvwnzxkocajdla","asd","ajdl","o;cajd","zvw","asdasdkasklzvwnzxkocajdla"))}
    PluginMain.logger.info{Json.encodeToString(AhoCorasickMatcherCopy().match("asdasdkasklzvwnzxkocajdla","asd","ajdl","ocajd","zvw"))}
}

fun initDatabase(){
    val db = DBUtils.getDatabase(Constant.databaseFile)
    DBUtils.initTableIfNotExist(db)
}