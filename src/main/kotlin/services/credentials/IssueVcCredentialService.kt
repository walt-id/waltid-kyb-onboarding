package id.walt.services.credentials

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

    private const val BASE_URL = "http://127.0.0.1:7002/"
    private val ISSUER_KEY = mapOf(
        "type" to "jwk",
        "jwk" to mapOf(
            "kty" to "OKP",
            "d" to "Poslg5iBF-qc53zgsLdxpqdRaO0Z14mt09a5ZA2NIgg",
            "crv" to "Ed25519",
            "kid" to "jVVEQr1EjUAFvHQCHumyoZ8WN4Myfz2elqGrSxhlUI4",
            "x" to "w4qaseqowtM19-5Z7bpyic8K5dQnrXi0izigqOCLGmc"
        )
    )

    private const val ISSUER_DID =
        "did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQWhSSG5DSnhsc0hMdUZQM3JWTTE2YWNJeXZvUFpkN3p1SGhLUkUtenBrayIsIngiOiJqQTZkZklSRzFodzdzQ2I4bUhyUWhadVB2RWlzZkhwLTBNSFpDUXZtRHhBIn0"


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
        http.post("${BASE_URL}openid4vc/jwt/issue") {
            setBody(requestBody)
        }.bodyAsText()

    suspend fun issue(
        legal_name: String,
        business_type: String,
        registration_address: String,
        registration_number: String,
        phone_number: String,
        website: String,
        business_did: String

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

        val silentExchange = http.post("http://127.0.0.1:7001/wallet-api/api/useOfferRequest/$business_did") {
            setBody(request)
        }.bodyAsText()

        println("silentExchange: $silentExchange")

        if (silentExchange.toInt() == 0) {
            throw Exception("Failed to issue VC")
        }

        return true

    }
}