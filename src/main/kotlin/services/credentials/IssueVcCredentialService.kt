package id.walt.services.credentials

import id.walt.commons.config.ConfigManager
import id.walt.crypto.utils.JsonUtils.toJsonElement
import id.walt.models.business.Business
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


object IssuerVcCredentialService {
    private val ISSUER_KEY = ConfigManager.getConfig<IssuerConfiguration>().issuerKey
    private val ISSUER_URL = ConfigManager.getConfig<IssuerConfiguration>().issuerUrl
    private val ISSUER_DID = ConfigManager.getConfig<IssuerConfiguration>().issuerDid


    private val http = HttpClient(CIO) {
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
    }


    suspend fun issue(
        business: Business,
        businessDid: String,
        termsAndConditions: String? = null
    ): Boolean {
        val credentialRequests = business.credentials.map { credentialType ->
            when {
                credentialType == "GaiaXTermsAndConditions" -> {
                    buildGaiaXTermsAndConditionsCredential(businessDid , termsAndConditions!!)

                }

                credentialType == "LegalPerson" -> {
                    buildLegalPersonCredential(business, businessDid)
                }

                credentialType == "LegalRegistrationNumber" -> {
                    buildLegalRegistrationNumberCredential(business, businessDid)
                }

                credentialType == "DataspaceParticipantCredential" -> {
                    buildDataSpaceParticipantCredential(business, businessDid)
                }

                else -> {
                    throw Exception("Unsupported credential type: $credentialType")
                }
            }
        }

        if (business.credentials.isEmpty()) {
            throw Exception("No valid credential requests found for types: ${business.credentials}")
        }

        val requestBody = credentialRequests.map { it.toJsonElement().jsonObject }


        val offerRequest = http.post("${ISSUER_URL}/openid4vc/jwt/issueBatch") {
            setBody(requestBody)
        }

        val silentExchange = http.post("${business.wallet_url}/wallet-api/api/useOfferRequest/$businessDid") {
            setBody(offerRequest.bodyAsText())
        }

        if (silentExchange.bodyAsText()
                .toIntOrNull() == 0 || !silentExchange.status.isSuccess()
        ) throw Exception("Failed to issue VCs for types ${business.credentials}")

        return true
    }


    @OptIn(ExperimentalTime::class)
    fun buildGaiaXTermsAndConditionsCredential(businessDid: String, termsAndConditions : String): JsonObject {
        return buildJwtIssueRequest(
            configurationId = "GaiaXTermsAndConditions_jwt_vc_json",
            credentialData = mapOf(
                "@context" to listOf(
                    "https://www.w3.org/2018/credentials/v1",
                    "https://w3id.org/security/suites/jws-2020/v1",
                    "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#"
                ),
                "id" to "urn:uuid:${UUID.randomUUID()}",
                "type" to listOf("VerifiableCredential", "GaiaXTermsAndConditions"),
                "issuer" to ISSUER_DID,
                "credentialSubject" to mapOf(
                    "gx:termsAndConditions" to termsAndConditions,
                    "type" to "gx:GaiaXTermsAndConditions",
                    "id" to businessDid
                )
            )
        )
    }

    @OptIn(ExperimentalTime::class)
    fun buildLegalPersonCredential(business: Business, businessDid: String): JsonObject {
        return buildJwtIssueRequest(
            configurationId = "LegalPerson_jwt_vc_json",
            credentialData = mapOf(
                "@context" to listOf(
                    "https://www.w3.org/2018/credentials/v1",
                    "https://w3id.org/gaia-x/development#"
                ),
                "id" to "urn:uuid:${UUID.randomUUID()}",
                "type" to listOf("VerifiableCredential", "LegalPerson"),
                "credentialSubject" to mapOf(
                    "id" to businessDid,
                    "type" to "gx:LegalPerson",
                    "gx:legalName" to business.legal_name,
                    "gx:legalRegistrationNumber" to mapOf("id" to business.registration_number),
                    "gx:headquarterAddress" to mapOf(
                        "gx:countrySubdivisionCode" to business.country_subdivision_code,
                        "gx:streetAddress" to business.street_address,
                    )
                )
            )
        )
    }

    @OptIn(ExperimentalTime::class)
    fun buildLegalRegistrationNumberCredential(business: Business, businessDid: String): JsonObject {
        val now = Clock.System.now()
        return buildJwtIssueRequest(
            configurationId = "LegalRegistrationNumber_jwt_vc_json",
            credentialData = mapOf(
                "@context" to listOf(
                    "https://www.w3.org/2018/credentials/v1",
                    "https://w3id.org/security/suites/jws-2020/v1"
                ),
                "id" to "urn:uuid:${UUID.randomUUID()}",
                "type" to listOf("VerifiableCredential", "LegalRegistrationNumber"),
                "issuer" to ISSUER_DID,
                "issuanceDate" to now.toString(),
                "credentialSubject" to mapOf(
                    "@context" to "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                    "type" to "gx:legalRegistrationNumber",
                    "id" to businessDid,
                    "gx:leiCode" to business.lei_code,
                    "gx:leiCode-countryCode" to business.address_country_code,
                    "gx:leiCode-legalName" to business.legal_name,
                    "gx:leiCode-legalAddress" to mapOf(
                        "gx:countrySubdivisionCode" to business.country_subdivision_code,
                        "gx:streetAddress" to business.street_address,
                        "gx:postalCode" to business.postal_code,
                        "gx:locality" to business.locality
                    )
                )
            )
        )
    }


    @OptIn(ExperimentalTime::class)
    fun buildDataSpaceParticipantCredential(business: Business, businessDid: String): JsonObject {
        val now = Clock.System.now()
        return buildJwtIssueRequest(
            configurationId = "DataspaceParticipantCredential_jwt_vc_json",
            credentialData = mapOf(
                "@context" to listOf(
                    "https://www.w3.org/2018/credentials/v1",
                    "https://w3id.org/security/suites/jws-2020/v1"
                ),
                "id" to "urn:uuid:${UUID.randomUUID()}",
                "type" to listOf("VerifiableCredential", "DataspaceParticipantCredential"),
                "issuer" to ISSUER_DID,
                "issuanceDate" to now.toString(),
                "credentialSubject" to mapOf(
                    "id" to businessDid,
                    "@context" to "https://registry.lab.gaia-x.eu/development/api/trusted-shape-registry/v1/shapes/jsonld/trustframework#",
                    "type" to "DataspaceParticipant",
                    "dataspaceId" to business.dataSpaceId,
                    "legalName" to business.legal_name,
                    "website" to business.website,
                    "legalAddress" to mapOf(
                        "countryCode" to business.address_country_code,
                        "streetAddress" to business.street_address,
                        "postalCode" to business.postal_code,
                        "locality" to business.locality
                    ),
                )
            )
        )
    }

    @OptIn(ExperimentalTime::class)
    private fun buildJwtIssueRequest(
        configurationId: String,
        credentialData: Map<String, Any?>,
    ): JsonObject = mapOf(
        "issuerKey" to ISSUER_KEY,
        "credentialConfigurationId" to configurationId,
        "credentialData" to credentialData,
        "mapping" to mapOf(
            "id" to "<uuid>",
            "credentialSubject" to mapOf("id" to "<subjectDid>"),
            "issuanceDate" to "<timestamp>",
            "expirationDate" to "<timestamp-in:365d>"
        ),
        "authenticationMethod" to "PRE_AUTHORIZED",
        "issuerDid" to ISSUER_DID,
        "standardVersion" to "DRAFT13"
    ).toJsonElement().jsonObject
}