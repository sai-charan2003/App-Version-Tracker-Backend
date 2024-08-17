package example.com.model.app_data

import kotlinx.serialization.Serializable

@Serializable
data class AppData(
    val appName : String,
    val appVersion : Int,
    val appVersionCode: Int? = null,
    val appDownloadLink : String
)