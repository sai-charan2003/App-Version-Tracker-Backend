package example.com.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import example.com.model.authentication.User
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.util.reflect.*
import java.io.File

class JWTConfig {
    //val dotEnv = Dotenv.configure().directory("C:\\Users\\saich\\Developer\\Ktor\\Auto_Update").filename("secrets.env").load()
    
    private val issuer = "AutoUpdateAPI"
    private val isDocker = !File("C:\\Users\\saich\\Developer\\Ktor\\Auto_Update\\secrets.env").exists()

    // Retrieve JWT secret based on the environment
    val jwtSecret: String = if (isDocker) {
        System.getenv("JWT_SECRET") ?: throw IllegalStateException("JWT_SECRET environment variable is not set")
    } else {
        val dotEnv = Dotenv.configure()
            .directory("C:\\Users\\saich\\Developer\\Ktor\\Auto_Update")
            .filename("secrets.env")
            .load()
        dotEnv["JWT_SECRET"] ?: throw IllegalStateException("JWT_SECRET not found in .env file")
    }
    private val algorithm = Algorithm.HMAC512(jwtSecret)
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