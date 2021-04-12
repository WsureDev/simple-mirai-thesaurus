package top.wsure.thesaurus

import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info
import org.jetbrains.exposed.sql.transactions.transaction
import top.wsure.thesaurus.mirai.data.Constant
import top.wsure.thesaurus.mirai.utils.ThesaurusUtils
import top.wsure.thesaurus.ws.thesaurus.data.Word
import top.wsure.thesaurus.ws.thesaurus.entity.Global
import top.wsure.thesaurus.ws.thesaurus.entity.GlobalTable
import top.wsure.thesaurus.ws.thesaurus.enums.MessageType
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

            Constant.THESAURUS_SERVICE.addThesaurus(Constant.GLOBAL_TAG, Word("qqq","aaa",MessageType.PRECISE),844157922L)
            transaction {
                val globals:List<Global> =  Global.find { GlobalTable.question eq "qqq" }.distinct()
                logger.info { globals.joinToString("\n") }
            }

        }
        globalEventChannel().subscribeAlways<MessageEvent> { ThesaurusUtils.handleMessageEvent(it) }

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