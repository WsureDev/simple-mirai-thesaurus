package top.wsure.thesaurus.ws.thesaurus.utils

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import top.wsure.thesaurus.mirai.data.Constant
import top.wsure.thesaurus.mirai.data.ThesaurusData
import top.wsure.thesaurus.ws.thesaurus.entity.GlobalTable
import top.wsure.thesaurus.ws.thesaurus.entity.GroupTable
import top.wsure.thesaurus.ws.thesaurus.entity.GroupsSettingTable
import java.io.File


/**

 * FileName: SqliteUtils
 * Author:   wsure
 * Date:     2021/1/21 5:10 下午
 * Description:
 */
class DBUtils {
    companion object {
        fun getDatabase(file: File): Database {
            val url = "jdbc:h2:file:${file.absolutePath};AUTO_SERVER=TRUE"
            return Database.connect(
                url = url,
                driver = Constant.driver,
                user = ThesaurusData.username,
                password = ThesaurusData.password
            )
        }

        fun initTableIfNotExist(db:Database ) {
            val tables = arrayListOf<Table>(GlobalTable,GroupTable,GroupsSettingTable)
            transaction(db) {
                addLogger(StdOutSqlLogger)
                tables.forEach {
                    SchemaUtils.create(it)
                }
            }
        }

    }

}