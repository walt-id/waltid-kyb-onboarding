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
            "wallet_did": "did:key:z6MkjoRhq1jSNJdLiruSXrFFxagqrztZaXHqHGUTKJbcNywp",
            "wallet_url" : "http://127.0.0.1:7001",
            "business_type": "Technology",
            "registration_address": "Walt Street 1",
            "registration_number": "1234567890",
            "email": "walt@id",
            "country_code": "US",
            "lei_code": "1234567890",
            "phone_number": "+1234567890",
            "website": "https://walt.id",
            "dataSpaceId": "walt",
            "credentials": [
                "LegalPerson",
                "LegalRegistrationNumber",
                "GaiaXTermsAndConditions"
            ]
            
        }
        """.trimIndent()

    )

    // language=json
    val approveBusinessExample = jsonObjectValueExampleDescriptorDsl(
        """
          {
          "businessUUID": "c6d2e689-5aad-4d87-b2d8-b333de285c04"
        }
        """.trimIndent()
    )

    // language=json
    val rejectBusinessExample = jsonObjectValueExampleDescriptorDsl(
        """
          {
          "businessUUID": "c6d2e689-5aad-4d87-b2d8-b333de285c04"
        }
        """.trimIndent()
    )

}