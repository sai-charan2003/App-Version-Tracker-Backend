package example.com.security

import io.ktor.util.*
import java.nio.charset.Charset
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

private val SECRET_KEY = "020836738"
private val ALGORITHM = "HmacSHA1"
private val HASH_KEY = hex(SECRET_KEY)
private val HMAC_KEY = SecretKeySpec(HASH_KEY, ALGORITHM)

fun hashPassword(password:String) : String{
    val hmac = Mac.getInstance(ALGORITHM)
    hmac.init(HMAC_KEY)
    return hex(hmac.doFinal(password.toByteArray(Charset.defaultCharset())))
}