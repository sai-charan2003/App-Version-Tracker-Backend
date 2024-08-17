package example.com

import example.com.plugins.*
import example.com.routes.authRoute
import example.com.security.configureSecurity
import io.ktor.http.*
import io.ktor.server.application.*


import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

}

fun Application.module() {
    configureSecurity()
    routing()
    configureSerialization()
    configureDatabase()
    install(CORS){
        anyHost() // Allows any host. Be cautious with this in production.
        allowHeader(HttpHeaders.Authorization) // Allow Authorization header if you're using JWT
        allowHeader(HttpHeaders.ContentType) // Allow Content-Type header
        allowMethod(HttpMethod.Options) // Preflight requests
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Head)
        allowNonSimpleContentTypes = true // Allow non-simple content types
        allowCredentials = true // Allow credentials
    }


}
