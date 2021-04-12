package top.wsure.thesaurus.mirai.data

import top.wsure.thesaurus.PluginMain
import top.wsure.thesaurus.mirai.utils.ThesaurusUtils
import top.wsure.thesaurus.ws.thesaurus.service.ThesaurusService
import top.wsure.thesaurus.ws.thesaurus.service.impl.ThesaurusServiceImpl
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AcNode

object Constant {
    const val driver = "org.h2.Driver"
    const val GLOBAL_KEY = "GLOBAL::"
    const val GLOBAL_TAG = -1L
    val databaseFile = PluginMain.resolveDataFile(ThesaurusData.databaseName)
    val THESAURUS_SERVICE: ThesaurusService by lazy { ThesaurusServiceImpl() }
    val CACHE : MutableMap<String, AcNode<String>> by lazy { ThesaurusUtils.createThesaurusCache() }
    val GROUPS_SETTINGS : MutableMap<Long,Pair<Boolean,Int>> by lazy { THESAURUS_SERVICE.getGroupSettings() }
}