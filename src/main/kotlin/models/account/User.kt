package id.walt.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class Account(
    @SerialName("_id")
    val id: String = ObjectId().toHexString(),
    val email: String,
    val password: String,
    val company: String,
    val role: String,
    val dataSpaceId: String,
)

