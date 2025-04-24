package id.walt.services.credentials

import id.walt.commons.config.ConfigManager
import id.walt.crypto.utils.JsonUtils.toJsonElement
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject


object IssuerVcCredentialService {
    private val ISSUER_KEY = ConfigManager.getConfig<IssuerConfiguration>().issuerKey
    private val BASE_URL = ConfigManager.getConfig<IssuerConfiguration>().issuerUrl
    private val ISSUER_DID = ConfigManager.getConfig<IssuerConfiguration>().issuerDid
    private val WALLET_URL = ConfigManager.getConfig<WalletConfiguration>().walletUrl

    private val http = HttpClient {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        expectSuccess = false
    }

    private suspend fun requestIssuance(requestBody: JsonObject) =
        http.post("${BASE_URL}/openid4vc/jwt/issue") {
            setBody(requestBody)
        }.bodyAsText()

    suspend fun issue(
        legal_name: String,
        business_type: String,
        registration_address: String,
        registration_number: String,
        phone_number: String,
        website: String,
        business_did: String,

        ): Boolean {
        val requestBody = mapOf(
            "issuerKey" to ISSUER_KEY,
            "issuerDid" to ISSUER_DID,
            "credentialConfigurationId" to "VerifiableId_jwt_vc_json",
            "credentialData" to mapOf(
                "@context" to listOf("https://www.w3.org/2018/credentials/v1"),
                "type" to listOf("VerifiableId"),
                "issuer" to mapOf(
                    "id" to ISSUER_DID,
                    "name" to "Walt KYB VC",
                    "url" to "https://walt.id",
                    "image" to mapOf(
                        "id" to "https://images.squarespace-cdn.com/content/v1/609c0ddf94bcc0278a7cbdb4/4d493ccf-c893-4882-925f-fda3256c38f4/Walt.id_Logo_transparent.png?format=1500w",
                        "type" to "Image",
                    )
                ),
                "credentialSubject" to mapOf(
                    "legal_name" to legal_name,
                    "business_type" to business_type,
                    "registration_address" to registration_address,
                    "registration_number" to registration_number,
                    "phone_number" to phone_number,
                    "website" to website,
                )
            ),
            "standardVersion" to "DRAFT13",
            "authenticationMethod" to "PRE_AUTHORIZED",
            "mapping" to mapOf(
                "id" to "<uuid>",
                "issuer" to mapOf("id" to "<issuerDid>"),
                "credentialSubject" to mapOf("id" to "<subjectDid>"),
                "issuanceDate" to "<timestamp>",
                "expirationDate" to "<timestamp-in:365d>"
            )
        ).toJsonElement().jsonObject


        val request = requestIssuance(requestBody)

        println("request: $request")

        val silentExchange = http.post("${WALLET_URL}/wallet-api/api/useOfferRequest/$business_did") {
            setBody(request)
        }.bodyAsText()

        println("silentExchange: $silentExchange")

        if (silentExchange.toInt() == 0) {
            throw Exception("Failed to issue VC")
        }

        return true

    }
}