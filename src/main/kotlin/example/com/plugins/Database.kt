package example.com.plugins

import example.com.model.DatabaseFactory
import io.ktor.server.application.*

fun Application.configureDatabase(){
    DatabaseFactory.init()

}