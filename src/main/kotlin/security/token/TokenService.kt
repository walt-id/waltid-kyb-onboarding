package id.walt.security.token

import id.walt.models.user.Account

interface TokenService {
    fun generateToken(Account: Account): String
}