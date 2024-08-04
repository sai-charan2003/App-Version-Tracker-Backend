package example.com.model.auto_update

import kotlinx.serialization.Serializable

@Serializable
data class AutoUpdate(
    val appName : String,
    val appVersion : Int,
    val appVersionCode: Int,
)