package top.wsure.thesaurus.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

class Global(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Global>(GlobalTable)
    var question by GlobalTable.question
    var answer by GlobalTable.answer
    var type by GlobalTable.type
    var createDate by GlobalTable.createDate
    var createUser by GlobalTable.createUser
}

object GlobalTable : LongIdTable("t_global") {
    var question = text("question")
    var answer = text("answer")
    var type = integer("type").default(0)
    val createDate = datetime("create_date").defaultExpression(CurrentDateTime())
    val createUser = long("create_user").nullable()
}
