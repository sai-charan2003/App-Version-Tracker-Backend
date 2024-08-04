package example.com.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(){
    JWTConfig().init()
    install(Authentication){
        jwt {
            verifier(JWTConfig().verifier)
            validate {
                if (it.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
        }
    }


}