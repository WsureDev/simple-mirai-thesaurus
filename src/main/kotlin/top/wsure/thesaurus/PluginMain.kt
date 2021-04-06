package top.wsure.thesaurus

import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.utils.info
import top.wsure.thesaurus.data.Constant
import top.wsure.thesaurus.entity.GlobalTable
import top.wsure.thesaurus.utils.DBUtils

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
}

fun initDatabase(){
    val db = DBUtils.getDatabase(Constant.databaseFile)
    DBUtils.initTableIfNotExist(db)
}