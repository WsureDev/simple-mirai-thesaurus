package top.wsure.thesaurus.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import top.wsure.thesaurus.entity.GlobalTable.clientDefault
import top.wsure.thesaurus.entity.GlobalTable.defaultExpression

class GroupsSetting(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Global>(GlobalTable)
    var groupId by GroupsSettingTable.groupId
    var enable by GroupsSettingTable.enable
    var editorLevel by GroupsSettingTable.editorLevel
    var createDate by GroupsSettingTable.createDate
    var createUser by GroupsSettingTable.createUser
}

object GroupsSettingTable : LongIdTable("t_groups_setting") {
    var groupId = long("group_id").uniqueIndex("uk_group_id")
    var enable = bool("enable").default(false)
    var editorLevel = integer("editor_level").default(1)
    val createDate = datetime("create_date").defaultExpression(CurrentDateTime())
    val createUser = long("create_user").nullable()
}
