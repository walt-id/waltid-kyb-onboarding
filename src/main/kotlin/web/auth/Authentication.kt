package id.walt.web.auth

import id.walt.commons.config.ConfigManager
import id.walt.commons.web.modules.AuthenticationServiceModule
import id.walt.ktorauthnz.KtorAuthnzManager
import id.walt.ktorauthnz.auth.devKtorAuthnzMocked
import id.walt.ktorauthnz.auth.ktorAuthnz
import id.walt.ktorauthnz.sessions.AuthSession
import id.walt.ktorauthnz.sessions.AuthSessionStatus
import id.walt.ktorauthnz.tokens.jwttoken.JwtTokenHandler
import id.walt.ktorauthnz.tokens.ktorauthnztoken.KtorAuthNzTokenHandler
import io.klogging.logger

private val log = logger("Authentication")

//const val EXAMPLE_TOKEN = "eyJhbGciOiJFZERTQSJ9.eyJzdWIiOiJhY2NvdW50MSIsInNlc3Npb24iOiJzZXNzaW9uSWQxIn0.b9Rua8aENhY9WhDeT1lZzXP5nXhWbky_ZzWA2Xsr3TrWhlfWpBBBA2aWsF0XgiaWQkYzcyC2K91c25bsrrqDCQ"
const val EXAMPLE_TOKEN = "dev-token"
const val EXAMPLE_ACCOUNT_UUID_STRING = "account1"

// Mocked authentication
private val mockedAuthentication: suspend () -> Unit = suspend {
    KtorAuthnzManager.sessionStore.wip_sessions["dev-session"] = AuthSession(
        id = "dev-session",
        status = AuthSessionStatus.OK,
        token = EXAMPLE_TOKEN,
        accountId = EXAMPLE_ACCOUNT_UUID_STRING
    )
    (KtorAuthnzManager.tokenHandler as KtorAuthNzTokenHandler).tokenStore.tokens[EXAMPLE_TOKEN] = "dev-session"

    AuthenticationServiceModule.AuthenticationServiceConfig.apply {
        customAuthentication = {
            devKtorAuthnzMocked(null, EXAMPLE_TOKEN) {
            }
        }
    }
}

// Actual authentication
private val authnzAuth: suspend () -> Unit = suspend {
    //KtorAuthnzManager.accountStore = AuthenticationService.accountStore
    // KtorAuthnzManager.accountStore = AuthenticationService.editableAccountStore

    val config = ConfigManager.getConfig<AuthenticationConfiguration>()
    val configSigningKey = config.configuredSigningKey
    val configVerificationKey = config.configuredVerificationKey

    KtorAuthnzManager.tokenHandler = JwtTokenHandler().apply {
        if (configSigningKey != null) {
            signingKey = configSigningKey
        }
        verificationKey = configVerificationKey
    }

    AuthenticationServiceModule.AuthenticationServiceConfig.apply {
        customAuthentication = {
            ktorAuthnz {

            }
        }
    }
}

val authenticationPluginAmendment: suspend () -> Unit = suspend {
    // Choose one here:

    /* Mocked authentication */
    //mockedAuthentication()

    /* Full authentication */
    authnzAuth()

    /*


    account1, tenant1:
    eyJhbGciOiJFZERTQSJ9.eyJzdWIiOiJhY2NvdW50MSIsInNlc3Npb24iOiJzZXNzaW9uMSIsInRlbmFudCI6ImFwcDEifQ.71mpofhR2M60nTHlMn5OvyxbFoFqrEJXhuUwKURmjjMsXknwL9ho4BPnpjlqUewptXUdBfxitjfaGMxoyYrMCQ


    account1:
    eyJhbGciOiJFZERTQSJ9.eyJzdWIiOiJhY2NvdW50MSIsInNlc3Npb24iOiJzZXNzaW9uSWQxIn0.A0VwiXJddRTSIuW5fF0tnWQhyoqpCzp1YjLQQ9Ly6RTziMRtdhn-lfTEPrChvr1_te9KqHjSJY31nDuPokoBAw

    account2:
    eyJhbGciOiJFZERTQSJ9.eyJzdWIiOiJhY2NvdW50MiIsInNlc3Npb24iOiJzZXNzaW9uMiJ9.3hT0fN7Ikog_uBnJVQ2dXlsilcyacFBnjFdbi1PmAanzC1ES7bXp0QUXpQmTIKlPvKjqukOBX1OaHlKPRQ4PBw

    {"sub":"account1","role":"vp.role.user","tenant":"tenant1"}:
    eyJhbGciOiJFZERTQSJ9.eyJzdWIiOiJhY2NvdW50MSIsInJvbGUiOiJ2cC5yb2xlLnVzZXIiLCJ0ZW5hbnQiOiJ0ZW5hbnQxIn0.HuWUeMhOHeX3z5gQKKpD9m59QmHX9p1bdFLc9MSHj_3q4C74u8Cn3jwCLvSttIzaYSAFyXQ6u2vPX9B-F7UyCA

    {"sub":"account1","role":"vp.role.service","tenant":"tenant1"}:
    eyJhbGciOiJFZERTQSJ9.eyJzdWIiOiJhY2NvdW50MSIsInJvbGUiOiJ2cC5yb2xlLnNlcnZpY2UiLCJ0ZW5hbnQiOiJ0ZW5hbnQxIn0.UgTw1j1-275PhDd3935g9Q0BUuhN2vA7IKzADmEV7v1noTOiSQq9dIIRWRv7xxxGDXKykTMFnf-wmw6-8BTKAw

     */
}