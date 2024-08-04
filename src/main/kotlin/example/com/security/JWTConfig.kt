package example.com.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import example.com.model.authentication.User
import io.ktor.util.reflect.*

class JWTConfig {
    private val issuer = "AutoUpdateAPI"
    private val secret= System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(secret)
    val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateJWTToken(user:User): String{
        return JWT.create()
            .withIssuer(issuer)
            .withClaim("username", user.emailId)
            .sign(algorithm)

    }

    lateinit var instance : JWTConfig
        private set

    fun init(){
        synchronized(this){
            if(!this::instance.isInitialized){
                instance = JWTConfig()
            }
        }
    }
}