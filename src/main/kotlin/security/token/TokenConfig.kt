package id.walt.security.token

data class TokenConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val expiresIn: Long
)
