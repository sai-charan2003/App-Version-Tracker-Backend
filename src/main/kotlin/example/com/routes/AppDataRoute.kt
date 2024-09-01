package example.com.routes

import example.com.model.app_data.AppData
import example.com.model.app_data.AppDataRepoImp
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.appDataRoute(appDataRepoImp: AppDataRepoImp) {
    routing {
        authenticate {
            route("/appData") {
                post("/postAppDetails") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        if(principal==null){
                            call.respond(HttpStatusCode.Unauthorized,"Not Authorized")
                        }
                        val userEmail = principal?.payload?.getClaim("username")?.asString()

                        if (userEmail == null) {
                            call.respond(HttpStatusCode.Unauthorized, "User email not found")
                            return@post
                        }

                        val updateData = call.receive<AppData>()

                        val existingData = appDataRepoImp.getDataByEmail(userEmail)
                        if (existingData.any { it?.appName == updateData.appName }) {
                            call.respond(HttpStatusCode.Conflict, "App details already exist")
                            return@post
                        }

                        appDataRepoImp.addData(updateData, userEmail)
                        call.respond(HttpStatusCode.Created, updateData)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "An error occurred while processing your request")
                    }
                }

                patch("/updateAppDetails") {
                    try {
                        val principal = call.principal<JWTPrincipal>()
                        val userEmail = principal?.payload?.getClaim("username")?.asString()

                        if (userEmail == null) {
                            call.respond(HttpStatusCode.Unauthorized, "User email not found")
                            return@patch
                        }

                        val updateData = call.receive<AppData>()

                        val existingData = appDataRepoImp.getDataByEmail(userEmail)
                        if (existingData.none { it?.appUUID == updateData.appUUID }) {
                            call.respond(HttpStatusCode.NotFound, "App not found")
                            return@patch
                        }

                        appDataRepoImp.updateData(updateData, userEmail)
                        call.respond(HttpStatusCode.OK, updateData)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "An error occurred while processing your request")
                    }
                }
                delete("/deleteAppDetails") {
                    try{
                        val principal = call.principal<JWTPrincipal>()
                        val userEmail = principal?.payload?.getClaim("username")?.asString()
                        if(principal==null){
                            call.respond(HttpStatusCode.Unauthorized,"Not Authorized")
                            return@delete
                        }
                        val appUUID = call.parameters["appUUID"]
                        if(appUUID!=null){
                            if (userEmail != null) {
                                appDataRepoImp.removeData(appUUID,userEmail)
                            }
                            call.respond(HttpStatusCode.OK,"Deleted")

                        }
                    } catch (ex:Exception){
                        ex.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "An error occurred while processing your request")

                    }
                }

            }
        }

        route("/getData") {
            get {
                try {
                    val apiKey = call.parameters["apiKey"]
                    val appName = call.parameters["appName"]

                    if (apiKey == null) {
                        call.respond(HttpStatusCode.BadRequest, "API Key not provided")
                        return@get
                    }
                    print(apiKey)



                    if (appName == null) {
                        val data = appDataRepoImp.getDataByAPI(apiKey)
                        println(data)
                        call.respond(HttpStatusCode.OK,data)

                        print(appDataRepoImp.getDataByAPI(apiKey))
                    } else {
                         val allAppData = appDataRepoImp.getDataByAppNameAndAPI(api = apiKey , appName = appName)
                        if(allAppData!=null) {
                            call.respond(allAppData)
                        } else{
                            call.respond(HttpStatusCode.NotFound, "No data found")
                        }
                    }

                    call.respond(HttpStatusCode.OK)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, "An error occurred while processing your request")
                }
            }
        }
    }
}
