package id.walt.web

import id.walt.routes.authRoute
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(DoubleReceive)
    install(RoutingRoot)

    routing {
        authRoute()
    }
}