package top.wsure.thesaurus.service

import top.wsure.thesaurus.entity.Global
import top.wsure.thesaurus.entity.Group

interface ThesaurusService {

    fun globalCache():List<Global>

    fun groupsCache(groupId:Long):List<Group>

}