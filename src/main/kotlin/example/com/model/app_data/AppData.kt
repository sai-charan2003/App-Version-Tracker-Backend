package example.com.model.app_data

import kotlinx.serialization.Serializable

@Serializable
data class AppData(
    val appName : String,
    val appVersion : Double,
    val appVersionCode: Double? = null,
    val appDownloadLink : String,
    val appUUID: String? = null,
//    val isMandatoryUpdate : Boolean? = null,
//    val notes : String? = null
)