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

                println("adminId: $adminId")
                val pendingCompanies = BusinessService().getPendingBusiness(adminId)
                call.respond(pendingCompanies)
            }

            post("/admin/companies/approve", {
                request {
                    body<JsonObject> {
                        description =
                            "Company to approve. The company will be approved with the provided information."

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
                val businessId = request["businessId"]?.jsonPrimitive?.content.toString()
                val accountId = call.principal<JWTPrincipal>()?.getClaim("adminId", String::class)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val approved = BusinessService().updateCompanyStatus(accountId, businessId, CompanyStatus.APPROVED)
                if (approved) {
                    call.respondText("Company approved")
                } else {
                    call.respondText("Company not found", status = HttpStatusCode.NotFound)
                }
            }

            post("/admin/companies/update-status") {
                val request = call.receive<Map<String, String>>()
                val companyId = request["companyId"] ?: return@post call.respondText(
                    "Missing companyId",
                    status = HttpStatusCode.BadRequest
                )
                val status = request["status"] ?: return@post call.respondText(
                    "Missing status",
                    status = HttpStatusCode.BadRequest
                )

                val newStatus = try {
                    CompanyStatus.valueOf(status.uppercase())
                } catch (e: IllegalArgumentException) {
                    return@post call.respondText(
                        "Invalid status. Use APPROVED or REJECTED.",
                        status = HttpStatusCode.BadRequest
                    )
                }

                val accountId = call.principal<JWTPrincipal>()?.getClaim("id", String::class)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val updated =
                    BusinessService().updateCompanyStatus(accountId, ObjectId(companyId).toString(), newStatus)
                if (updated) {
                    call.respondText("Company status updated to $status")
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