package id.walt.models.business

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*


enum class CompanyStatus {
    PENDING, APPROVED, REJECTED
}

@Serializable
data class Business(
    val uuid: String = UUID.randomUUID().toString(),
    val legal_name: String,
    val wallet_did: String,
    val wallet_url: String,
    val business_type: String,
    val registration_number: String,
    val phone_number: String,
    val country_subdivision_code: String,
    val address_country_code: String,
    val street_address: String? = null,
    val postal_code: String? = null,
    val locality: String? = null,
    val lei_code: String,
    val email: String,
    val website: String,
    val approved: Boolean? = false,
    val approved_by: String? = null,
    @Contextual val dataSpaceId: String? = null,
    val status: CompanyStatus = CompanyStatus.PENDING,
    val credentials: List<String> = listOf(
        "LegalPerson",
        "LegalRegistrationNumber",
        "GaiaXTermsAndConditions"
    ),
    )
