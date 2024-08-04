package example.com.model.authentication

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val emailId:String,
    val password: String,
    val userName: String,
    var token : String?=null,
    var apiKey : String?=null
)