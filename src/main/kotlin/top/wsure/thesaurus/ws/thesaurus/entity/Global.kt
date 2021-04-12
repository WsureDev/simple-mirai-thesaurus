package top.wsure.thesaurus.ws.thesaurus.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime

class Global(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Global>(GlobalTable)
    var question by GlobalTable.question
    var answer by GlobalTable.answer
    var type by GlobalTable.type
    var createDate by GlobalTable.createDate
    var createUser by GlobalTable.createUser

    constructor(row:ResultRow):this(row[GlobalTable.id])

    override fun toString(): String {
        return "{" +
            "question:\"${question}\","+
        "answer:\"${answer}\","+
        "type:\"${type}\","+
        "createDate:\"${createDate}\","+
        "createUser:\"${createUser}\""+
            "}"
    }
}

object GlobalTable : LongIdTable("t_global") {
    var question = text("question")
    var answer = text("answer")
    var type = integer("type").default(0)
    val createDate = datetime("create_date").defaultExpression(CurrentDateTime())
    val createUser = long("create_user").nullable()
}
