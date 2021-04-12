package top.wsure.thesaurus.mirai.data

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object ThesaurusData: AutoSavePluginConfig("ThesaurusData") {
    val databaseName by value("thesaurus")
    var username  by value( "Wsure")
    var password by value( "Wsure")
}