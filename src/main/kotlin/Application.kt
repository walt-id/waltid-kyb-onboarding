package id.walt

import id.walt.database.configureDatabases
import id.walt.web.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main(args: Array<String>) {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@Suppress("unused") // application.conf references the main function.
fun Application.module() {
    configuration()

}

fun Application.configuration() {
    configureRouting()
    configureSecurity()
    configureHTTP()
   // configureOpenApi()
    configureSerialization()
    configureDatabases()
    configureStatusPages()

}
