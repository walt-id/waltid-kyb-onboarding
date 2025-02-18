package id.walt.services.business

import id.walt.models.business.Business
import id.walt.services.Examples
import id.walt.services.account.ExampleUser
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject

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
            BusinessService().createBusiness(business)
            call.respond(HttpStatusCode.Created)
        }

    }


    fun Route.registerRoutes() = route("Business", {
        tags("Business")
    }) {
        initRoutes()
    }


}