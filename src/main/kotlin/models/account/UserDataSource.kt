package id.walt.models.user

import id.walt.models.account.AccountDTO

interface UserDataSource {
    suspend fun createUser(user: Account): Account
    suspend fun getUserByEmail(email: String): Account?
    suspend fun getUserById(id: String): Account?
    suspend fun deleteUserById(id: String): Boolean
    suspend fun getAllUsers(): List<AccountDTO>
    companion object {
        val users = mutableListOf<Account>()
    }
}