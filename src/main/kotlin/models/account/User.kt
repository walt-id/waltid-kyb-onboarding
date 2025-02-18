package id.walt.models.user
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Serializable
data class Account @OptIn(ExperimentalUuidApi::class) constructor(
    val email: String,
    val password: String,
    val company: String,
    val role: String,
)

