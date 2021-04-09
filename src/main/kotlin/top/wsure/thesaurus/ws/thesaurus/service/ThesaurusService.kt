package top.wsure.thesaurus.ws.thesaurus.service

import top.wsure.thesaurus.ws.thesaurus.entity.Global
import top.wsure.thesaurus.ws.thesaurus.entity.Group
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AcNode

interface ThesaurusService {

    fun globalCache():AcNode<Global>

    fun groupsCache(groupId:Long):List<Group>

}