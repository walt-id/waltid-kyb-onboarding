package id.walt.data.models.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val email: String,
    val password: String
)