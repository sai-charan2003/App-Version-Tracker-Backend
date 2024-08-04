package example.com.plugins


import example.com.model.authentication.UserRepoImp
import example.com.model.auto_update.AutoUpdate
import example.com.model.auto_update.AutoUpdateRepoImp
import example.com.routes.appDataRoute
import example.com.routes.authRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.routing() {
    val userRepoImp = UserRepoImp()
    val autoUpdateRepoImp = AutoUpdateRepoImp(userRepoImp)
    authRoute(userRepoImp)
    appDataRoute(autoUpdateRepoImp)

}
