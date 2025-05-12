package id.walt.services.account

import id.walt.models.user.Account
import id.walt.security.token.JwtTokenService
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.mindrot.jbcrypt.BCrypt
import kotlin.uuid.ExperimentalUuidApi

object UserController {


    @OptIn(ExperimentalUuidApi::class)
    private fun Route.initRoutes() {

        post<Account>("/register", {
            summary = "Register a dataspace owner"
            description = "Add a new dataspace owner to the system"
            request {
                body<JsonObject> {
                    description =
                        "User to add. The user will be registered with the provided information."
                    example("Minimal example", ExampleUser.createUserRequestBodyExample)

                }
            }
            response {
                HttpStatusCode.Created to {
                    description = "Dataspace owner added"
                }
            }
        }) {
            val account = call.receive<JsonObject>()
            val user = Account(
                email = account["email"]?.jsonPrimitive?.content?.trim()?.lowercase() ?: "",
                password = BCrypt.hashpw(account["password"]?.jsonPrimitive?.content ?: "", BCrypt.gensalt()),
                company = account["company"]?.jsonPrimitive?.content ?: "",
                role = account["role"]?.jsonPrimitive?.content ?: "",
                dataSpace = account["dataSpace"]?.jsonPrimitive?.content ?: ""
            )


            UserService().createUser(user)
            println("user created")
            call.respond(HttpStatusCode.Created)
        }


        post("/login", {
            summary = "Login Dataspace owner"
            description = "Login a Dataspace owner to the system"
            request {
                body<JsonObject> {
                    description =
                        "User to login. The user will be logged in with the provided information."
                    example("Minimal example", ExampleUser.loginUserRequestBodyExample)
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Successful Request"
                    body<String> {
                        description = "the response"

                    }
                }
                HttpStatusCode.InternalServerError to {
                    description = "Something unexpected happened"
                }
            }
        }) {
            runCatching {

                val request = call.receive<JsonObject>()

                val email =
                    request["email"] ?: return@post call.respondText(
                        "Missing email",
                        status = HttpStatusCode.BadRequest
                    )
                val password = request["password"] ?: return@post call.respondText(
                    "Missing password",
                    status = HttpStatusCode.BadRequest
                )
                val account = UserService().authenticate(email.jsonPrimitive.content, password.jsonPrimitive.content)
                val token = JwtTokenService().generateToken(
                    account
                )
                call.respond(mapOf("token" to token))
                call.respond(HttpStatusCode.OK)
            }.onFailure {
                throw it
            }
        }

        get("/all") {
            call.respond(UserService().getAllUsers())
        }


        authenticate("auth-jwt") {
            get("/id") {
                val principal = call.principal<JWTPrincipal>()
                val adminId = principal!!.payload.getClaim("adminId").asString()
                call.respond(mapOf("adminId" to adminId))
            }
        }




    }

    fun Route.registerRoutes() = route("operator", {
        tags("Operator")
    }) {
        initRoutes()
    }

}