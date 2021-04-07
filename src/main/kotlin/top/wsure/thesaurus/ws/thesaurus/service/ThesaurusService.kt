package top.wsure.thesaurus.ws.thesaurus.service

import top.wsure.thesaurus.ws.thesaurus.entity.Global
import top.wsure.thesaurus.ws.thesaurus.entity.Group

interface ThesaurusService {

    fun globalCache():List<Global>

    fun groupsCache(groupId:Long):List<Group>

}