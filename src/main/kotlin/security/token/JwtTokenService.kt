package id.walt.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import id.walt.models.user.Account
import java.util.*

class JwtTokenService : TokenService {
    override fun generateToken(
        Account: Account
    ): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("adminId", Account.id.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
            .sign(Algorithm.HMAC256(secret))
    }

}

//TODO : move this from here
private const val secret = "123456789" // Change this in production
private const val issuer = "ktor-app"
private const val audience = "ktor-users"
private const val validityInMs = 36_000_00 * 10 // 10 hours