package id.walt

import id.walt.commons.ServiceConfiguration
import id.walt.commons.ServiceInitialization
import id.walt.commons.ServiceMain
import id.walt.commons.config.ConfigManager
import id.walt.commons.featureflag.CommonsFeatureCatalog
import id.walt.commons.web.WebService
import id.walt.database.Database
import id.walt.database.DatabaseConfiguration
import id.walt.did.helpers.WaltidServices
import id.walt.web.auth.authenticationPluginAmendment
import id.walt.web.configureHTTP
import id.walt.web.configureRouting
import id.walt.web.configureSecurity
import id.walt.web.kybApi
import id.walt.web.openApiPluginAmendment
import io.ktor.server.application.*

suspend fun main(args: Array<String>) {
    ServiceMain(
        ServiceConfiguration("Visitor Pass"),
        ServiceInitialization(
            features = listOf(FeatureCatalog),
            featureAmendments = mapOf(
                CommonsFeatureCatalog.authenticationServiceFeature to authenticationPluginAmendment,
                CommonsFeatureCatalog.openApiFeature to openApiPluginAmendment
            ), init = {
                val dbConfig = ConfigManager.getConfig<DatabaseConfiguration>()
                Database.connect(dbConfig).use()

                /*DB.drop()
                  DB.create()
                 DbInitService.ingestDefaultData()*/
                WaltidServices.minimalInit()
            }, run = WebService(Application::module).run()
        )
    ).main(args)
}
fun Application.module() {
    configuration()
}


fun Application.configuration() {
    configureRouting()
    configureSecurity()
    configureHTTP()
    // configureOpenApi()
    //configureSerialization()
    //  configureDatabases()
    // configureStatusPages()
    kybApi()

}
