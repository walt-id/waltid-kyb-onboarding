package id.walt.routes


import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*


internal fun Application.authRoute() {

    routing {
        route("/r/user") {

            get("/test") {
                val message = "Hello, World!"
                call.respondText(message)
            }
        }
    }
}