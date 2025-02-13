package id.walt.database

import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import id.walt.crypto.keys.jwk.JWKKey
import id.walt.models.user.Account
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseConfiguration(
    val connectionString: String
)

object Database {

    private lateinit var mongoClient: MongoClient

    private val database by lazy { mongoClient.getDatabase("waltid-KYB") }

    fun connect(dbConfig: DatabaseConfiguration): Database {
        mongoClient = MongoClient.create(dbConfig.connectionString)
        database
        return Database
    }

    lateinit var accounts: MongoCollection<Account>

    private val collectionNames = listOf("accounts", "credentials")
    fun use() {
        accounts = database.getCollection<Account>("accounts")
    }

    private fun getCollectionReference(name: String) = database.getCollection<Unit>(name)


    suspend fun setup() {
        suspend fun createCollections(vararg name: String) =
            name.forEach { database.createCollection(it) }

        suspend fun createIndexes(vararg indexes: Pair<String, List<String>>) =
            indexes.forEach { (collectionName, indexes) ->
                getCollectionReference(collectionName).let { collection ->
                    indexes.forEach { indexName -> collection.createIndex(Indexes.ascending(indexName)) }
                }
            }

        createCollections(*collectionNames.toTypedArray())

    }

    suspend fun drop() {
        suspend fun dropCollections(vararg name: String) =
            name.forEach { getCollectionReference(it).drop() }

        dropCollections(*collectionNames.toTypedArray())
    }

    suspend fun insertExampleData() {
//        val key = JWKKey.importJWK(exampleKey).getOrThrow()
//        val accountKey = AccountKey(key.getKeyId(), DirectSerializedKey(key))
//
//        val req = InitWalletRequest(did = exampleDid, keys = listOf(accountKey))
//        InitializationService.initAccount("account1", req)
//        CredentialService.storeCredentials("account1", ExampleData.storeCredentials.asFlow())
    }

}