package example.com.routes

import example.com.model.auto_update.AutoUpdate
import example.com.model.auto_update.AutoUpdateRepoImp
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.text.get

fun Application.appDataRoute(autoUpdateRepoImp: AutoUpdateRepoImp) {

    routing {
        authenticate {
            route("/postAppDetails") {
                post {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userEmail = principal?.payload?.getClaim("username")?.asString()
                        println(userEmail)

                        val updateData = call.receive<AutoUpdate>()
                        if(autoUpdateRepoImp.getDataByEmail(userEmail!!).any { it?.appName == updateData.appName }){
                            call.respond("App details already exists")
                            call.respond(HttpStatusCode.Conflict)
                        }
                        autoUpdateRepoImp.addData(updateData,userEmail)
                        call.respond(updateData)
                    } catch (ex: Exception) {
                        println(ex)
                        call.respond(HttpStatusCode.BadGateway)
                    }
                }
                patch {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userEmail = principal?.payload?.getClaim("username")?.asString()
                        val updateData = call.receive<AutoUpdate>()
                        if(!autoUpdateRepoImp.getDataByEmail(userEmail!!).any { it?.appName == updateData.appName }){
                            call.respond("App Not Found")
                            call.respond(HttpStatusCode.Conflict)

                        }
                        autoUpdateRepoImp.updateData(updateData,userEmail!!)
                        call.respond(updateData)

                    } catch (ex: Exception) {
                        println(ex)
                        call.respond(HttpStatusCode.BadGateway)
                    }
                }

            }




        }

        route("/getData") {

            get {
                try {
                    val apiKey = call.parameters["apiKey"]
                    if (apiKey != null) {
                        val allData = autoUpdateRepoImp.getDataByAPI(apiKey)
                        call.respond(allData)
                    } else {
                        call.respond("API Key not found")
                        call.respond(HttpStatusCode.BadRequest)
                    }

                } catch (ex: Exception) {
                    call.respond(HttpStatusCode.BadGateway)
                }
            }
        }
    }
}