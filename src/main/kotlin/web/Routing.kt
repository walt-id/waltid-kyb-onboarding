package id.walt.web

import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(DoubleReceive)

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
    }
}