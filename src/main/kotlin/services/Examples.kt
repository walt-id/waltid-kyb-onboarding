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
            "business_type": "Technology",
            "registration_address": "Walt Street 1",
            "registration_number": "1234567890",
            "email": "walt@id",
            "country_code": "US",
            "lei_code": "1234567890",
            "phone_number": "+1234567890",
            "website": "https://walt.id",
            "adminId": "5f7b1b3b7f7b7b7b7b7b7b7b"
            
        }
        """.trimIndent()

    )

    // language=json
    val businessUpdateRequestBodyExample = jsonObjectValueExampleDescriptorDsl(
        """
          {
          "registration_number": "ABC123",
          "credentialTypes": ["GaiaXTermsAndConditions", "LegalPerson", "custom"],
          "customCredential": {
            "@context": [
              "https://www.w3.org/2018/credentials/v1",
              "https://example.org/custom-context"
            ],
            "type": ["VerifiableCredential", "CustomType"],
            "credentialSubject": {
              "id": "did:web:example.org",
              "customAttribute": "customValue"
            }
          }
        }
        """.trimIndent()
    )

}