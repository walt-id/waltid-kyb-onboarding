package id.walt.services

import io.github.smiley4.ktorswaggerui.dsl.routes.ValueExampleDescriptorDsl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

object Examples {


    private fun jsonObjectValueExampleDescriptorDsl(content: String): ValueExampleDescriptorDsl.() -> Unit = {
        value = Json.decodeFromString<JsonObject>(content)
    }

    // language=json
    val createBusinessRequestBodyExample = jsonObjectValueExampleDescriptorDsl(
        """
        {
            "legal_name": "Walt ID",
            "business_type": "Technology",
            "registration_address": "Walt Street 1",
            "registration_number": "1234567890",
            "email": "walt@id",
            "phone_number": "+1234567890",
            "website": "https://walt.id"
            
        }
        """.trimIndent()

    )

}