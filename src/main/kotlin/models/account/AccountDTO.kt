package id.walt.models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDTO(
    @SerialName("_id") val id: String,
    val email: String,
    val company: String,
    val role: String,
    val dataSpace: String
)

