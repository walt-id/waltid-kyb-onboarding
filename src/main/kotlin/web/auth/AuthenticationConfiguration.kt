package id.walt.web.auth

import id.walt.crypto.keys.KeyManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class AuthenticationConfiguration(
    val signingKey: JsonObject?,
    val verificationKey: JsonObject
) {
    val configuredSigningKey by lazy { signingKey?.let { KeyManager.resolveSerializedKeyBlocking(it.toString()) } }
    val configuredVerificationKey by lazy { KeyManager.resolveSerializedKeyBlocking(verificationKey.toString()) }
}

/*
suspend fun main() {
    println(KeySerialization.serializeKey(JWKKey.generate(KeyType.Ed25519)))
}
*/