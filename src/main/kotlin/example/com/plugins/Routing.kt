package example.com.plugins


import example.com.model.authentication.UserRepoImp
import example.com.model.app_data.AppDataRepoImp
import example.com.routes.appDataRoute
import example.com.routes.authRoute
import io.ktor.server.application.*

fun Application.routing() {
    val userRepoImp = UserRepoImp()
    val autoUpdateRepoImp = AppDataRepoImp(userRepoImp)
    authRoute(userRepoImp)
    appDataRoute(autoUpdateRepoImp)

}
