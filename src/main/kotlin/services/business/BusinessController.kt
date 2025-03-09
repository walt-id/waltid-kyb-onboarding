package id.walt.services.business

import id.walt.models.business.Business
import id.walt.models.business.CompanyStatus
import id.walt.services.Examples
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bson.types.ObjectId

object BusinessController {

    private fun Route.initRoutes() {
        post<Business>({
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
            val adminId = business.adminId.toString()
            BusinessService().createBusiness(business = business, adminId = adminId)
            call.respond(HttpStatusCode.Created)
        }


        authenticate("auth-jwt") {

            get("/admin/companies/pending") {
                val principal = call.principal<JWTPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val adminId =
                    principal.getClaim("adminId", String::class) ?: return@get call.respond(HttpStatusCode.Unauthorized)


                val pendingCompanies = BusinessService().getPendingBusiness(adminId)
                call.respond(pendingCompanies)
            }

            post("/admin/companies/approve", {
                request {
                    body<JsonObject> {
                        description =
                            "Company to approve. The company will be approved with the provided information."
                        example("Minimal example", Examples.businessUpdateRequestBodyExample)
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
                val businessId = request["registration_number"]?.jsonPrimitive?.content.toString()
                val accountId = call.principal<JWTPrincipal>()?.getClaim("adminId", String::class)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val approved = BusinessService().approveAndIssueVC(accountId, businessId)
                if (approved) {
                    call.respondText("Company approved and VC issued")
                } else {
                    call.respondText("Company not found", status = HttpStatusCode.NotFound)
                }
            }


            post("/admin/companies/reject", {
                request {
                    body<JsonObject> {
                        description =
                            "Company to reject. The company will be rejected with the provided information."
                        example("Minimal example", Examples.businessUpdateRequestBodyExample)
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
                val businessId = request["registration_number"]?.jsonPrimitive?.content.toString()
                val accountId = call.principal<JWTPrincipal>()?.getClaim("adminId", String::class)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val updated =
                    BusinessService().updateCompanyStatus(
                        accountId,
                        ObjectId(businessId).toString(),
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


    fun Route.registerRoutes() = route("Business", {
        tags("Business")
    }) {
        initRoutes()
    }


}