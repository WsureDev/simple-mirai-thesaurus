package top.wsure.thesaurus.ws.thesaurus.service.impl

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import top.wsure.thesaurus.mirai.data.Constant
import top.wsure.thesaurus.ws.thesaurus.data.Word
import top.wsure.thesaurus.ws.thesaurus.entity.*
import top.wsure.thesaurus.ws.thesaurus.enums.MessageType
import top.wsure.thesaurus.ws.thesaurus.service.ThesaurusService
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AcNode
import top.wsure.thesaurus.ws.thesaurus.utils.ac.AhoCorasickMatcher

class ThesaurusServiceImpl : ThesaurusService {
    override fun globalCache(): AcNode<String> {
        val questions: Set<String> = transaction {
            val query = GlobalTable
                .slice(GlobalTable.question)
                .selectAll()
                .andWhere { GlobalTable.type eq MessageType.FUZZY.type }
                .distinct()
            query.map { it[GlobalTable.question] }.toSet()
        }
        return createAcMatcher(questions)
    }

    override fun groupsCache(groupId: Long): AcNode<String> {
        val questions: Set<String> = transaction {
            val query = GroupTable
                .slice(GroupTable.question)
                .selectAll()
                .andWhere { GroupTable.type eq MessageType.FUZZY.type }
                .andWhere { GroupTable.groupId eq groupId }
                .distinct()
            query.map { it[GroupTable.question] }.toSet()
        }
        return createAcMatcher(questions)
    }

    override fun getGroupSettings(): MutableMap<Long, Pair<Boolean, Int>> {
        return transaction {
            val groupSettings = HashMap<Long, Pair<Boolean, Int>>()
            GroupsSettingTable
                .selectAll()
                .forEach {
                    groupSettings[it[GroupsSettingTable.groupId]] =
                        Pair(it[GroupsSettingTable.enable], it[GroupsSettingTable.editorLevel])
                }
            groupSettings
        }
    }

    override fun queryThesaurus(groupId: Long, word: Word): List<Word> {
        return transaction {
            if (groupId == Constant.GLOBAL_TAG) {
                GlobalTable.select { GlobalTable.question eq word.question }
                    .andWhere { GlobalTable.type eq word.type.type }
                    .map { Word(it[GlobalTable.question], it[GlobalTable.answer], it[GlobalTable.type]) }
            } else {
                GroupTable.select { GroupTable.groupId eq groupId }
                    .andWhere { GroupTable.question eq word.question }
                    .andWhere { GroupTable.type eq word.type.type }
                    .map { Word(it[GroupTable.question], it[GroupTable.answer], it[GroupTable.type]) }
            }
        }
    }

    override fun addThesaurus(groupId: Long, word: Word, userId: Long) {
        return transaction {
            if (groupId == Constant.GLOBAL_TAG) {
                Global.new {
                    question = word.question
                    answer = word.answer
                    type = word.type.type
                    createUser = userId
                }
            } else {
                Group.new {
                    this.groupId = groupId
                    question = word.question
                    answer = word.answer
                    type = word.type.type
                    createUser = userId
                }
            }
        }
    }

    override fun delThesaurus(groupId: Long, word: Word): Int {
        return transaction {
            if (groupId == Constant.GLOBAL_TAG) {
                Global.find {
                    Op.build { GlobalTable.question eq word.question }
                        .and { GlobalTable.type eq word.type.type }
                }.map { it.delete() }.count()
            } else {
                Group.find {
                    Op.build { GroupTable.groupId eq groupId }
                        .and { GroupTable.question eq word.question }
                        .and { GroupTable.type eq word.type.type }
                }.map { it.delete() }.count()
            }
        }
    }

    override fun initGroupSetting(groupId: Long, enable: Boolean) {
        transaction {
            GroupsSetting.new {
                this.groupId = groupId
                this.enable = enable
                editorLevel = 1
                createDate = DateTime.now()
            }
        }
    }

    override fun setGroupSetting(groupId: Long, enable: Boolean) {
        transaction {
            val groupsSetting = GroupsSetting.find { GroupsSettingTable.groupId eq groupId }.firstOrNull()
            if (groupsSetting == null) {
                initGroupSetting(groupId, enable)
            } else {
                groupsSetting.enable = enable
            }
        }
    }

    private fun createAcMatcher(collection: Collection<String>): AcNode<String> {
        return AhoCorasickMatcher<String> { it }.constructACAutomaton(collection)
    }

}