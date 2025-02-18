package id.walt.models.business

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


enum class CompanyStatus {
    PENDING, APPROVED, REJECTED
}
@Serializable
data class Business(
    val legal_name: String,
    val business_type: String,
    val registration_address: String,
    val registration_number : String,
    val phone_number : String,
    val email : String,
    val website : String,
    val approved : Boolean? = false,
    val approved_by : String? = null,
    @Contextual val adminId: ObjectId? = null,
    val status: CompanyStatus = CompanyStatus.PENDING

)
