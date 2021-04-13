package top.wsure.thesaurus.ws.thesaurus.service

import top.wsure.thesaurus.ws.thesaurus.data.Word
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AcNode

interface ThesaurusService {

    fun globalCache():AcNode<String>

    fun groupsCache(groupId:Long):AcNode<String>

    fun getGroupSettings():MutableMap<Long,Pair<Boolean,Int>>

    fun queryThesaurus(groupId: Long, word: Word):List<Word>

    fun addThesaurus(groupId: Long, word: Word,userId:Long)

    fun delThesaurus(groupId: Long, word: Word)

    fun initGroupSetting(groupId: Long)
}