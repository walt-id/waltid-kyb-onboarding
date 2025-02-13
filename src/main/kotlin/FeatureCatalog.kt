package id.walt

import id.walt.commons.featureflag.BaseFeature
import id.walt.commons.featureflag.OptionalFeature
import id.walt.commons.featureflag.ServiceFeatureCatalog
import id.walt.database.DatabaseConfiguration
import id.walt.web.auth.AuthenticationConfiguration

object FeatureCatalog : ServiceFeatureCatalog {

    val databaseFeature = BaseFeature("database", "Database", DatabaseConfiguration::class)
    val authenticationFeature = BaseFeature("auth", "Authentication", AuthenticationConfiguration::class)

    override val baseFeatures = listOf(databaseFeature, authenticationFeature)
    override val optionalFeatures = listOf<OptionalFeature>()

}