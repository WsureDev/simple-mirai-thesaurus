package top.wsure.thesaurus.ws.thesaurus.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime

class Group(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Group>(GroupTable)
    var groupId by GroupTable.groupId
    var question by GroupTable.question
    var answer by GroupTable.answer
    var type by GroupTable.type
    var createDate by GroupTable.createDate
    var createUser by GroupTable.createUser
}

object GroupTable : LongIdTable("t_group") {
    var groupId = long("group_id")
    var question = text("question")
    var answer = text("answer")
    var type = integer("type").default(0)
    val createDate = datetime("create_date").defaultExpression(CurrentDateTime())
    val createUser = long("create_user").nullable()
}
