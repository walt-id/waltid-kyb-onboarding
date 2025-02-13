package id.walt.security.hashing

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

class SHA256HashingService : HashingService {
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        // Generate a random salt
        val salt = ByteArray(saltLength)
        SecureRandom().nextBytes(salt)

        // Create a SHA-256 hash instance
        val digest = MessageDigest.getInstance("SHA-256")

        // Combine the salt with the value (e.g., value + salt)
        digest.update(salt)
        val hashedBytes = digest.digest(value.toByteArray())

        // Encode the salt and hash as Base64 strings
        val base64Salt = Base64.getEncoder().encodeToString(salt)
        val base64Hash = Base64.getEncoder().encodeToString(hashedBytes)


        return SaltedHash(base64Salt, base64Hash)

    }

    override fun verifySaltedHash(
        value: String,
        saltedHash: SaltedHash
    ): Boolean {

// Decode the base64 encoded salt and hash
        val salt = Base64.getDecoder().decode(saltedHash.salt)
        val originalHash = Base64.getDecoder().decode(saltedHash.hash)

        // Create a SHA-256 hash instance
        val digest = MessageDigest.getInstance("SHA-256")

        // Combine the salt with the provided value (e.g., value + salt)
        digest.update(salt)
        val computedHash = digest.digest(value.toByteArray())

        // Compare the computed hash with the original hash
        return computedHash.contentEquals(originalHash)
    }


}

fun main() {
    val value = "mySecretPassword"
    val saltLength = 16 // 16 bytes long salt
    val saltedHash = SHA256HashingService().generateSaltedHash(value, saltLength)

    println("Salt: ${saltedHash.salt}")
    println("Hash: ${saltedHash.hash}")

    // Verify the hash
    val isVerified = SHA256HashingService().verifySaltedHash(value, saltedHash)
    println("Hash Verified: $isVerified")
}