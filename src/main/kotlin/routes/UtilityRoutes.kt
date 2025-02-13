package id.walt.routes
import io.github.smiley4.ktorswaggerui.dsl.routing.get
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject

object UtilityController {

    fun Route.registerRoutes() = route({ tags("Utility") }) {
        get("/log", {
            summary = "Log"
            description = "Log and debugging information, for internal use."
            request {

            }
            response {
                HttpStatusCode.OK to {
                    description = "Successful Request"
                    body<String> { description = "the response" }
                }
                HttpStatusCode.InternalServerError to {
                    description = "Something unexpected happened"
                }
            }
        }) {
            call.respond(HttpStatusCode.OK)
        }

        get("/health", {
            summary = "Health"
            description = "Service health, for internal use."
            request {
            }
            response {
                HttpStatusCode.OK to {
                    description = "Successful Request"
                    body<String> { description = "the response" }
                }
                HttpStatusCode.InternalServerError to {
                    description = "Something unexpected happened"
                }
            }
        }) {
            call.respond(HttpStatusCode.OK)
        }

//        get("/stats", {
//            summary = "Statistics"
//            description = "Usage statistics per user, tbd."
//            request {
//            }
//            response {
//                HttpStatusCode.OK to {
//                    description = "Successful Request"
//                    body<StatsResponse> { description = "the response" }
//                }
//                HttpStatusCode.InternalServerError to {
//                    description = "Something unexpected happened"
//                }
//            }
//        }) {
//            call.respond(HttpStatusCode.OK)
//        }

//        post("/sign", {
//            summary = "Sign"
//            description = "Signs arbitrary JSON data."
//            request {
//                body<JsonObject> {
//                    description = "Data to be signed"
//                    example("Basic example", Examples.signRequestExample)
//                }
//            }
//            response {
//                HttpStatusCode.OK to {
//                    description = "Successful Request"
//                    body<String> {
//                        description = "the response"
//                        //example("Basic example", Examples.signResponseExample)
//                    }
//                }
//                HttpStatusCode.InternalServerError to {
//                    description = "Something unexpected happened"
//                }
//            }
//        }) {
//            runCatching {
//                val body = context.receive<JsonObject>()
//                val signedCredential = SigningService.executeSigning(body)
//                context.respond(HttpStatusCode.OK, signedCredential)
//            }.onFailure {
//                throw it
//            }
//        }

//        post("/verifySignature", {
//            summary = "Verify"
//            description = "Verifies arbitrary JSON data."
//            request {
//                body<VerifyRequest>()
//            }
//            response {
//                HttpStatusCode.OK to {
//                    description = "Successful Request"
//                    body<VerifyResponse> { description = "the response" }
//                }
//                HttpStatusCode.InternalServerError to {
//                    description = "Something unexpected happened"
//                }
//            }
//        }) {
//            call.respond(HttpStatusCode.OK)
//        }

//        get("current-id", {
//            summary = "Shows ID of token"
//            response { HttpStatusCode.OK to { body<String> { description = "ID" } } }
//        }) {
//            val (_, accountId) = getTenantAndAccount(ROLE_USER)
//            context.respond(accountId)
//        }
    }

}