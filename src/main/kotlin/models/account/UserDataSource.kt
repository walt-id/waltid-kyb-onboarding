package id.walt.models.user

interface UserDataSource {
    suspend fun createUser(user: Account): Account
    suspend fun getUserByEmail(email: String): Account?
    suspend fun getUserById(id: String): Account?
    suspend fun deleteUserById(id: String): Boolean

    companion object {
        val users = mutableListOf<Account>()
    }
}