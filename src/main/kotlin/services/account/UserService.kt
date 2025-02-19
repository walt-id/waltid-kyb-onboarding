package id.walt.services.account

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import id.walt.database.Database
import id.walt.models.user.Account
import id.walt.models.user.UserDataSource
import kotlinx.coroutines.flow.firstOrNull
import org.mindrot.jbcrypt.BCrypt
import kotlin.uuid.ExperimentalUuidApi


class UserService() : UserDataSource {

    suspend fun isAccountExisting(email: String): Boolean =
        Database.accounts.countDocuments(eq("email", email)) >= 1


    suspend fun authenticate(email: String, password: String): Account? {
        val admin = Database.accounts.find(Filters.eq("email", email)).firstOrNull() ?: return null
        return if (BCrypt.checkpw(password, admin.password)) admin else null
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createUser(user: Account): Account {
        if (isAccountExisting(user.email)) {
            error("Account with  ${user.email} already exists")
        }


        Database.accounts.insertOne(user)
        return user
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


