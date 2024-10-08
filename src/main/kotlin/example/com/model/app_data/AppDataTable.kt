package example.com.model.app_data

import org.jetbrains.exposed.sql.Table

object AppDataTable:Table() {
    val appName = varchar("appName",512)
    val appVersion = double("appVersion")
    val appVersionCode = double("appVersionCode").nullable()
    val id = integer("id").autoIncrement()
    val appDownloadLink = varchar("appDownloadLink",512)
    val apiKey = varchar("apiKey",512)
    val appUUID = varchar("appUUID",512).nullable()
//    val isMandatoryUpdate = bool("isMandatoryUpdate").nullable()
//    val notes = text("notes").nullable()
    override val primaryKey = PrimaryKey(id)
}