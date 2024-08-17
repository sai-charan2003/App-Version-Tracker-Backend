package example.com.model.app_data

import org.jetbrains.exposed.sql.Table

object AppDataTable:Table() {
    val appName = varchar("appName",512)
    val appVersion = integer("appVersion")
    val appVersionCode = integer("appVersionCode").nullable()
    val id = integer("id").autoIncrement()
    val appDownloadLink = varchar("appDownloadLink",512)
    val apiKey = varchar("apiKey",512)
    override val primaryKey = PrimaryKey(id)
}