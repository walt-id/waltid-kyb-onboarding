package id.walt.web

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun Application.configureSecurity() {
   authentication {
         basic(name = "auth") {
              realm = "Ktor Server"
              validate { credentials ->
                if (credentials.name == "user" && credentials.password == "password") {
                     UserIdPrincipal(credentials.name)
                } else {
                     null
                }
              }
         }

   }
}
