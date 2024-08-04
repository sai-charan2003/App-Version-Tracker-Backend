package example.com.model.authentication

import org.jetbrains.exposed.sql.Table

object UserTable: Table("userDetails") {
    val emailId = varchar("emailId",512)
    val password = varchar("password",512)
    val userName = varchar("userName",512)
    val apiKey = varchar("apiKey",512)
    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
}