package example.com.routes

import example.com.model.authentication.User
import example.com.model.authentication.UserRepoImp
import example.com.security.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoute(userRepoImp: UserRepoImp) {
    routing {
        route("/register") {
            post {
                try {
                    val user = call.receive<User>()

                    if (userRepoImp.isUserExists(user)) {
                        call.respond(HttpStatusCode.Conflict, "Email already exists")
                        return@post
                    }

                    val userFromDB = userRepoImp.createUser(user)
                    if (userFromDB != null) {
                        userFromDB.token = JWTConfig().generateJWTToken(user)
                        call.respond(HttpStatusCode.Created, userFromDB)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "User creation failed")
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    call.respond(HttpStatusCode.BadRequest, "Invalid request data")
                }
            }
        }

        route("/login") {
            post {
                try {
                    val user = call.receive<User>()

                    if (!userRepoImp.isUserExists(user)) {
                        call.respond(HttpStatusCode.NotFound, "Email does not exist")
                        return@post
                    }

                    val loginData = userRepoImp.loginUser(user)
                    if (loginData == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Incorrect Password")
                    } else {
                        loginData.token = JWTConfig().generateJWTToken(loginData)
                        call.respond(HttpStatusCode.OK, loginData)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.BadRequest, "Invalid request data")
                }
            }
        }

    }
}
