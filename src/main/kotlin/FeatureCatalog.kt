package id.walt

import id.walt.commons.featureflag.BaseFeature
import id.walt.commons.featureflag.OptionalFeature
import id.walt.commons.featureflag.ServiceFeatureCatalog
import id.walt.database.DatabaseConfiguration
import id.walt.services.credentials.IssuerConfiguration
import id.walt.web.auth.AuthenticationConfiguration

object FeatureCatalog : ServiceFeatureCatalog {

    val databaseFeature = BaseFeature("database", "Database", DatabaseConfiguration::class)
    val authenticationFeature = BaseFeature("auth", "Authentication", AuthenticationConfiguration::class)
    val issuerFeature = BaseFeature("issuer", "Issuer", IssuerConfiguration::class)

    override val baseFeatures = listOf(databaseFeature, authenticationFeature, issuerFeature)
    override val optionalFeatures = listOf<OptionalFeature>()

}