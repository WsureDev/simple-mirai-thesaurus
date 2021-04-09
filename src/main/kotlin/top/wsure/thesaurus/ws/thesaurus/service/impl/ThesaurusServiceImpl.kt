package top.wsure.thesaurus.ws.thesaurus.service.impl

import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import top.wsure.thesaurus.ws.thesaurus.entity.Global
import top.wsure.thesaurus.ws.thesaurus.entity.GlobalTable
import top.wsure.thesaurus.ws.thesaurus.entity.Group
import top.wsure.thesaurus.ws.thesaurus.enums.MessageType
import top.wsure.thesaurus.ws.thesaurus.service.ThesaurusService
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AcNode

class ThesaurusServiceImpl : ThesaurusService {
    override fun globalCache(): AcNode<Global> {
        val questions : Set<String> = transaction {
            val query = GlobalTable
                .slice( GlobalTable.question)
                .selectAll()
                .andWhere { GlobalTable.type eq MessageType.FUZZY.type }
                .distinct()
            query.map { it[GlobalTable.question] }.toSet()
        }
        val charSet : Set<Char> = questions.map { it.toCharArray().asList() }.flatten().toSet()
        TODO("Not yet implemented")
    }

    override fun groupsCache(groupId: Long): List<Group> {
        TODO("Not yet implemented")
    }
}