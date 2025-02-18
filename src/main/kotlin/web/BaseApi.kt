package id.walt.web

import id.walt.services.account.UserController
import id.walt.services.business.BusinessController
import io.github.smiley4.ktorswaggerui.dsl.routing.route
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.kybApi() {
    routing {
        route("v1", {

        }) {

            UserController.run { registerRoutes() }
            BusinessController.run { registerRoutes() }

        }
    }
}