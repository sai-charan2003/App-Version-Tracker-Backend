package example.com.plugins

import example.com.model.auto_update.AutoUpdateRepo
import example.com.model.auto_update.AutoUpdateRepoImp
import example.com.model.DatabaseFactory
import example.com.model.authentication.UserRepo
import example.com.model.authentication.UserRepoImp
import io.ktor.server.application.*

fun Application.configureDatabase(){
    DatabaseFactory.init()

}