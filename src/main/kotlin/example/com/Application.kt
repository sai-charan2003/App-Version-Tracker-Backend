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
        anyHost()
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)

        allowNonSimpleContentTypes = true
    }


}
