package id.walt.services.business

import id.walt.models.business.Business
import id.walt.models.business.CompanyStatus
import id.walt.services.Examples
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

object BusinessController {

    private fun Route.initRoutes() {
        post<Business>("/register", {
            summary = "Register Business"
            description = "Add a new Business to the system"
            request {
                body<JsonObject> {
                    description =
                        "Business to add. The business will be registered with the provided information."
                    example("Minimal example", Examples.createBusinessRequestBodyExample)

                }
            }
            response {
                HttpStatusCode.Created to {
                    description = "Business added"
                }
            }
        }) {
            val business = call.receive<Business>()
            val dataSpaceId = business.dataSpaceId.toString()
            BusinessService().createBusiness(business = business, dataSpaceId = dataSpaceId)
            call.respond(HttpStatusCode.Created)
        }


        authenticate("auth-jwt") {

            get("/pending") {
                val principal = call.principal<JWTPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val dataSpaceId =
                    principal.getClaim("dataSpaceId", String::class)
                        ?: return@get call.respond(HttpStatusCode.Unauthorized)


                val pendingCompanies = BusinessService().getPendingBusiness(dataSpaceId)
                call.respond(pendingCompanies)
            }

            post("/approve", {
                summary = "Approve Business"
                description = "Approve a Business and issue a Verifiable Credential (VC)"
                request {
                    body<JsonObject> {
                        description = "Company to approve. The company will be approved with the provided information."
                        example("Minimal example", Examples.approveBusinessExample)
                    }
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Successful Request"
                        body<String> {
                            description = "The response body"
                        }
                    }
                    HttpStatusCode.InternalServerError to {
                        description = "Something unexpected happened"
                    }
                }
            }) {
                val request = call.receive<JsonObject>()
                val termsAndConditions = (request["termsAndConditions"] as? JsonPrimitive)
                    ?.contentOrNull ?: "The PARTICIPANT signing the Self-Description agrees as follows:\\n- to update its descriptions about any changes, be it technical, organizational, or legal - especially but not limited to contractual in regards to the indicated attributes present in the descriptions.\\n\\nThe keypair used to sign Verifiable Credentials will be revoked where Gaia-X Association becomes aware of any inaccurate statements in regards to the claims which result in a non-compliance with the Trust Framework and policy rules defined in the Policy Rules and Labelling Document (PRLD)."
                val businessUUID = request["businessUUID"]?.jsonPrimitive?.content
                    ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing registration_number")

                val accountId = call.principal<JWTPrincipal>()?.getClaim("dataSpaceId", String::class)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)



                val approved = BusinessService().approveAndIssueVC(
                    accountId,
                    businessUUID,
                    termsAndConditions
                )

                if (approved) {
                    call.respondText("Company approved and VC issued")
                } else {
                    call.respondText("Company not found", status = HttpStatusCode.NotFound)
                }
            }


            post("/reject", {
                summary = "Reject Business"
                description = "Reject a Business"
                request {
                    body<JsonObject> {
                        description =
                            "Company to reject. The company will be rejected with the provided information."
                        example("Minimal example", Examples.rejectBusinessExample)
                    }
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Successful Request"
                        body<String> {
                            description = "the response body"

                        }
                    }
                    HttpStatusCode.InternalServerError to {
                        description = "Something unexpected happened"
                    }
                }
            }) {
                val request = call.receive<JsonObject>()
                val businessUUID = request["businessUUID"]?.jsonPrimitive?.content.toString()
                val dataSpaceId = call.principal<JWTPrincipal>()?.getClaim("dataSpaceId", String::class)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val updated =
                    BusinessService().updateCompanyStatus(
                        dataSpaceId,
                        businessUUID,
                        CompanyStatus.REJECTED
                    )
                if (updated) {
                    call.respondText("Company rejected")
                } else {
                    call.respondText("Company not found", status = HttpStatusCode.NotFound)
                }
            }
        }


    }


    fun Route.registerRoutes() = route("business", {
        tags("Business")
    }) {
        initRoutes()
    }


}