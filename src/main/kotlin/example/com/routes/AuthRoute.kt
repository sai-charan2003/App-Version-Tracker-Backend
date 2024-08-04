package example.com.routes

import example.com.model.authentication.User
import example.com.model.authentication.UserRepoImp
import example.com.model.auto_update.AutoUpdate
import example.com.security.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoute(userRepoImp: UserRepoImp){

    routing {
        route("/register"){
            post {
                try{
                    val user = call.receive<User>()
                    if (userRepoImp.isUserExists(user)){
                        call.respond("Email already exists")
                        call.respond(HttpStatusCode.Conflict)
                    } else {
                        val userFromDB = userRepoImp.createUser(user)
                        userFromDB?.token = JWTConfig().generateJWTToken(user)
                        call.respond(userFromDB!!)
                        call.respond(HttpStatusCode.Accepted)
                    }

                } catch (ex : Exception){
                    println(ex)
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
        route("/login"){
            post {
                try{
                    val user = call.receive<User>()
                    if(!userRepoImp.isUserExists(user)){
                        call.respond("Email does not exists")
                        call.respond(HttpStatusCode.Conflict)

                    } else{
                        val loginData = userRepoImp.loginUser(user)
                        if(loginData == null){
                            call.respond("Incorrect Password")
                            call.respond(HttpStatusCode.Unauthorized)
                        } else{
                            loginData.token = JWTConfig().generateJWTToken(loginData)
                            call.respond(loginData)
                            call.respond(HttpStatusCode.Accepted)
                        }
                    }
                } catch (e:Exception){
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}