package id.walt.security.token

interface TokenService {
    fun generateToken(config: TokenConfig,claims: List<TokenClaim>): String
}