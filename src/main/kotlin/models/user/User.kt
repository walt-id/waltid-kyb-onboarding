package id.walt.models.user
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class User(
    @BsonId val id: Id<User> = newId(),
    val name: String,
    val email: String,
    val password: String
)