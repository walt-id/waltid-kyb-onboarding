plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    application
}


group = "id.walt"
version = "0.0.1"

application {
    mainClass.set("id.walt.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}
tasks.shadowJar {
    isZip64 = true
}
repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.waltid.dev/releases")
    maven("https://maven.waltid.dev/snapshots")
}



object Versions {
    const val KTOR_VERSION = "3.1.0" // also change 1 plugin
    const val WALTID_VERSION = "0.12.0"//"1.0.2503041241-SNAPSHOT"//"1.0.2503041428-SNAPSHOT"//""0.11.0"
}

dependencies {
// Ktor Swagger
    implementation("io.ktor:ktor-server-openapi:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-swagger:${Versions.KTOR_VERSION}")
    implementation("io.github.smiley4:ktor-swagger-ui:2.3.1")
    api("io.github.smiley4:ktor-swagger-ui:3.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(platform("org.kotlincrypto.hash:bom:0.6.1"))
    implementation("org.kotlincrypto.hash:sha2")

    // Logging
    implementation("io.github.oshai:kotlin-logging:5.1.0")
    // Security
    implementation("org.mindrot:jbcrypt:0.4")



    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.2.0")
    implementation("org.mongodb:bson-kotlinx:5.2.0")

    // Ktor server
    implementation("io.ktor:ktor-server-core-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-auth-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-double-receive-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-host-common-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-status-pages-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-cors-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-forwarded-header-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-call-logging-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-call-id-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-cio-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-status-pages-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-compression-jvm:${Versions.KTOR_VERSION}")

    // ktor-client
    implementation("io.ktor:ktor-client-core:${Versions.KTOR_VERSION}") // Or latest version
    implementation("io.ktor:ktor-client-cio:${Versions.KTOR_VERSION}") // Or another engine
    implementation("io.ktor:ktor-client-content-negotiation:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-logging:${Versions.KTOR_VERSION}")

    // OpenAPI
    api("io.github.smiley4:ktor-swagger-ui:3.5.1")

    // JSON
    implementation("io.github.optimumcode:json-schema-validator:0.2.3")
    implementation("com.eygraber:jsonpathkt-kotlinx:3.0.2")
    implementation("io.github.reidsync:kotlin-json-patch:1.0.0")

    // Kotlinx
    implementation("app.softwork:kotlinx-uuid-core:0.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    // DB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.2.0")
    implementation("org.mongodb:bson-kotlinx:5.2.0")

    // Logging
    implementation("io.klogging:klogging:0.9.4")
    implementation("io.klogging:slf4j-klogging:0.9.4")
    implementation("org.slf4j:jul-to-slf4j:2.0.16")

    // walt.id
    implementation("id.walt.crypto:waltid-crypto:${Versions.WALTID_VERSION}")
    implementation("id.walt.credentials:waltid-verifiable-credentials:${Versions.WALTID_VERSION}")
    implementation("id.walt.did:waltid-did:${Versions.WALTID_VERSION}")
    implementation("id.walt.sdjwt:waltid-sdjwt:${Versions.WALTID_VERSION}")
    implementation("id.walt.openid4vc:waltid-openid4vc:${Versions.WALTID_VERSION}")
    implementation("id.walt:waltid-ktor-authnz:${Versions.WALTID_VERSION}")
    implementation("id.walt.permissions:waltid-permissions:${Versions.WALTID_VERSION}")
    implementation("id.walt:waltid-service-commons:${Versions.WALTID_VERSION}")
    implementation("id.walt.dif-definitions-parser:waltid-dif-definitions-parser:${Versions.WALTID_VERSION}")
    implementation("id.walt.wallet:waltid-core-wallet:${Versions.WALTID_VERSION}")


    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test-jvm:1.9.0")
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:2.3.12")
    testImplementation("io.ktor:ktor-client-cio:2.3.12")
    testImplementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    testImplementation("io.ktor:ktor-client-logging:2.3.12")

}


