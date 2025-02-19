package id.walt.models.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import kotlin.uuid.ExperimentalUuidApi


@Serializable
data class Account @OptIn(ExperimentalUuidApi::class) constructor(
    @SerialName("_id")
    @Contextual val id: ObjectId = ObjectId(),
    val email: String,
    val password: String,
    val company: String,
    val role: String,
)

