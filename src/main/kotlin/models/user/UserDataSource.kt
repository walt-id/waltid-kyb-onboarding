package id.walt.models.user

interface UserDataSource {
    suspend fun createUser(user: User): User
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserById(id: String): User?
    suspend fun deleteUserById(id: String): Boolean

    companion object {
        val users = mutableListOf<User>()
    }
}