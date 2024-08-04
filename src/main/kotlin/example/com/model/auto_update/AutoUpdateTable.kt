package example.com.model.auto_update

import org.jetbrains.exposed.sql.Table

object AutoUpdateTable:Table() {
    val appName = varchar("appName",512)
    val appVersion = integer("appVersion")
    val appVersionCode = integer("appVersionCode")
    val id = integer("id").autoIncrement()
    val apiKey = varchar("apiKey",512)
    override val primaryKey = PrimaryKey(id)
}