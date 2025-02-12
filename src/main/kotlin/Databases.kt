package id.walt

import id.walt.data.models.user.MongoUserDataSoure
import id.walt.data.models.user.User
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

@OptIn(DelicateCoroutinesApi::class)
fun Application.configureDatabases() {

    try {
        val client = KMongo.createClient("mongodb://root:password@localhost:27017/waltid-kyb?authSource=admin&authMechanism=SCRAM-SHA-256").coroutine

        val database = client.getDatabase("waltid-kyb")
        val userDataSource = MongoUserDataSoure(database)


        GlobalScope.launch {
            try {
                val user = User(
                    email = "test@email.com",
                    password = "password",
                    name = "John"
                )
                userDataSource.createUser(user)
                println("Test user created: ${user.name}")
            } catch (e: Exception) {
                println("Failed to create test user: ${e.message}")
            }
        }
    } catch (e: Exception) {
        println("Failed to connect to MongoDB: ${e.message}")
        throw e
    }

}


