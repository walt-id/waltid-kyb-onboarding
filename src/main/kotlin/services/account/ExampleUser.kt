package id.walt.services.account

import io.github.smiley4.ktorswaggerui.data.ExampleDescriptor
import io.github.smiley4.ktorswaggerui.dsl.routes.ValueExampleDescriptorDsl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject


object ExampleUser {
    private fun jsonObjectValueExampleDescriptorDsl(content: String): ValueExampleDescriptorDsl.() -> Unit = {
        value = Json.decodeFromString<JsonObject>(content)
    }
    // language=json
    val createUserRequestBodyExample = jsonObjectValueExampleDescriptorDsl(
        """
        {
            "email": "user@example.com",
            "password": "securepassword123",
            "company": "Walt ID",
            "role": "admin"
        }
        """.trimIndent()

    )

}