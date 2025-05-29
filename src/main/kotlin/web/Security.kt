package id.walt.web

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


private const val secret = "123456789" // Change this in production
private const val issuer = "ktor-app"
private const val audience = "ktor-users"
private const val validityInMs = 36_000_00 * 10 // 10 hours
fun Application.configureSecurity() {
    authentication {
        jwt("auth-jwt") {
            realm = issuer
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("dataSpaceId")
                        .asString() != null
                ) JWTPrincipal(credential.payload) else null
            }
        }

    }
}
