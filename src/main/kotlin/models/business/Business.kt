package id.walt.models.business

import kotlinx.serialization.Serializable

@Serializable
data class Business(
    val id: String,
    val legal_name: String,
    val business_type: String,
    val registration_address: String,
    val registration_number : String,
    val phone_number : String,
    val email : String,
    val website : String,
    val approved : Boolean,
    val approved_by : String,

)
