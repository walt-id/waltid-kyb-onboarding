package id.walt.services

import id.walt.models.user.User
import id.walt.models.user.UserDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserService(
    db : CoroutineDatabase
) : UserDataSource {

    private val userCollection = db.getCollection<User>()


    override suspend fun createUser(user: User): User {
        userCollection.insertOne(user).wasAcknowledged()
        return user
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userCollection.findOne(User::email eq email)
    }

    override suspend fun getUserById(id: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserById(id: String): Boolean {
        TODO("Not yet implemented")
    }

}