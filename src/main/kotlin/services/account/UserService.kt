package id.walt.services.account

import com.mongodb.client.model.Filters.eq
import id.walt.database.Database
import id.walt.models.account.AccountDTO
import id.walt.models.user.Account
import id.walt.models.user.UserDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.mindrot.jbcrypt.BCrypt
import kotlin.uuid.ExperimentalUuidApi


class UserService() : UserDataSource {

    suspend fun isAccountExisting(email: String): Boolean =
        Database.accounts.countDocuments(eq("email", email)) >= 1


    suspend fun authenticate(email: String, password: String): Account {
        val account = Database.accounts.find(eq("email", email)).firstOrNull()
            ?: throw IllegalArgumentException("Invalid credentials")

        if (!BCrypt.checkpw(password, account.password)) {
            throw IllegalArgumentException("Invalid credentials")
        }

        return account
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createUser(user: Account): Account {
        if (isAccountExisting(user.email)) {
            error("Account with  ${user.email} already exists")
        }
        Database.accounts.insertOne(user)
        return user
    }

    override suspend fun getAllUsers(): List<AccountDTO> {
        return Database.accounts.find()
            .toList()
            .map { account ->
                AccountDTO(
                    id = account.id,
                    email = account.email,
                    company = account.company,
                    role = account.role,
                    dataSpaceId = account.dataSpaceId
                )
            }
    }

    override suspend fun getUserByEmail(email: String): Account? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: String): Account? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserById(id: String): Boolean {
        TODO("Not yet implemented")
    }


}


