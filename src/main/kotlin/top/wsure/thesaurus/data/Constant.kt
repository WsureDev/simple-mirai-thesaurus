package top.wsure.thesaurus.data

import top.wsure.thesaurus.PluginMain

object Constant {
    val databaseFile = PluginMain.resolveDataFile(ThesaurusData.databaseName)
    const val driver = "org.h2.Driver"
    val CACHE : MutableMap<String,Any> = HashMap()
}