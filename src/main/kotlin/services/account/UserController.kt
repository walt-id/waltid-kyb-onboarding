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
//        get({
//            summary = "Get initialized wallet information"
//            description = "Get initialization information about an already initialized wallet"
//            response {
//                HttpStatusCode.OK to { body<Account>() }
//            }
//        }) {
//            val (_, accountId) = getTenantAndAccount(ROLE_USER, ROLE_SERVICE)
//            val account = InitializationService.getAccount(accountId) ?: throw BadRequestException("Wallet not initialized")
//
//            val redactedAccount = account.copy(keys = account.keys.map { accountKey ->
//                if (accountKey.data.key !is AzureKey) accountKey
//                else {
//                    val originalKey = accountKey.data.key as AzureKey
//
//                    val newKey = AzureKey(
//                        id = originalKey.id,
//                        auth = AzureAuth("[redacted]", "[redacted]", "[redacted]", "[redacted]"),
//                        _keyType = originalKey.keyType,
//                        _publicKey = DirectSerializedKey(originalKey.getPublicKey())
//                    )
//
//                    accountKey.copy(data = DirectSerializedKey(newKey))
//                }
//            })
//
//            context.respond(redactedAccount)
//        }

        post<Account>({
            summary = "Register User"
            description = "Add a new User to the system"
            request {
                body<JsonObject> {
                    description =
                        "User to add. The user will be registered with the provided information."
                    example("Minimal example", ExampleUser.createUserRequestBodyExample)

                }
            }
            response {
                HttpStatusCode.Created to {
                    description = "User added"
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
            summary = "Login User"
            description = "Login a User to the system"
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
                println("email: $email , password : $password")
                val account = UserService().authenticate(email.jsonPrimitive.content, password.jsonPrimitive.content)
                println("account: $account")
                val token = JwtTokenService().generateToken(
                    account
                )
                call.respond(mapOf("token" to token))
                call.respond(HttpStatusCode.OK)
            }.onFailure {
                throw it
            }
        }

        get("/admins") {
            call.respond(UserService().getAllUsers())
        }


        authenticate("auth-jwt") {
            get("/admin/profile") {
                val principal = call.principal<JWTPrincipal>()
                val adminId = principal!!.payload.getClaim("adminId").asString()
                call.respond(mapOf("adminId" to adminId))
            }
        }




    }

    fun Route.registerRoutes() = route("User", {
        tags("Users")
    }) {
        initRoutes()
    }

}