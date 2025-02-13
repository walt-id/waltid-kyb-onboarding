package id.walt.security.hashing

interface HashingService {
    fun generateSaltedHash(value: String, saltLength: Int): SaltedHash
    fun verifySaltedHash(value: String, saltedHash: SaltedHash): Boolean
}