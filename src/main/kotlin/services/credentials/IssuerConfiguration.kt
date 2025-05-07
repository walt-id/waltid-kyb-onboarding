package id.walt.services.credentials

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class IssuerConfiguration(
    val issuerKey: JsonObject,
    val issuerUrl: String,
    val issuerDid: String,
)

