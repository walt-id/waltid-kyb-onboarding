package id.walt.services.account

import id.walt.models.user.Account
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
               // id = Uuid.random().toString(),
                email = account["email"].toString(),
                password = account["password"].toString(),
                company = account["company"].toString(),
                role = account["role"].toString()
            )


            UserService().createUser(user)
            println("user created")
            call.respond(HttpStatusCode.Created)
        }


    }

    fun Route.registerRoutes() = route("User", {
        tags("Users")
    }) {
        initRoutes()
    }

}