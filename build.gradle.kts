
plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "2.3.13"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    application
}


object Versions {

    const val WALTID_VERSION = "0.12.0"

    const val ktorVersion = "3.1.2"
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

dependencies {

    // DB
    //MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.2.0")
    implementation("org.mongodb:bson-kotlinx:5.2.0")
    // Ktor Server
    implementation("io.ktor:ktor-client-core:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-core-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-auth-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-host-common-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-status-pages-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-cors-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-default-headers-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-openapi:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-swagger:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-serialization-gson-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-call-logging-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-call-id-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-double-receive-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-cio-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-server-sessions-jvm:${Versions.ktorVersion}")

    // Ktor Client
    implementation("io.ktor:ktor-client-core-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-client-serialization-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-client-json-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-client-cio-jvm:${Versions.ktorVersion}")
    implementation("io.ktor:ktor-client-logging-jvm:${Versions.ktorVersion}")

    // Ktor Swagger
    implementation("io.github.smiley4:ktor-swagger-ui:2.3.1")

    // Date/Time & Cryptography
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation(platform("org.kotlincrypto.hash:bom:0.6.1"))
    implementation("org.kotlincrypto.hash:sha2")

    // Logging
    implementation("io.github.oshai:kotlin-logging:5.1.0")
    // Security
    implementation("org.mindrot:jbcrypt:0.4")
    // WaltID dependencies
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

    // Testing
    testImplementation(kotlin("test"))
    api("io.github.smiley4:ktor-swagger-ui:3.5.1")
}

//fun MavenArtifactRepository.configureCredentials() {
//    val repoName = "MAVEN"
//    val (username, password) = waltidPrivateCredentials(repoName)
//    require(username.isNotEmpty()) { getMissingRepoCredentialsErrorMessage(repoName, "username") }
//    require(password.isNotEmpty()) { getMissingRepoCredentialsErrorMessage(repoName, "password") }
//    credentials {
//        this.username = username
//        this.password = password
//    }
//}
//fun waltidPrivateCredentials(repoName:String): Pair<String, String> = let {
//    val envUsername = System.getenv(repoName.uppercase() + "_USERNAME")
//    val envPassword = System.getenv(repoName.uppercase() + "_PASSWORD")
//
//    val usernameFile = File("$rootDir/secret-${repoName.lowercase()}-username.txt")
//    val passwordFile = File("$rootDir/secret-${repoName.lowercase()}-password.txt")
//
//    return Pair(
//        envUsername ?: usernameFile.let { if (it.isFile) it.readLines().first() else "" },
//        envPassword ?: passwordFile.let { if (it.isFile) it.readLines().first() else "" }
//    )
//}
// fun getMissingRepoCredentialsErrorMessage(repo: String, field: String) =
//    """
//        Missing $repo credentials.
//        Expecting non-empty value in:
//            - environment variable: ${repo.uppercase()}_${field.uppercase()}
//            - or text file in the project root: secret-${repo.lowercase()}-${field.lowercase()}.txt
//    """.trimIndent()
//ktor {
//    fatJar {
//        archiveFileName.set("kyb-onboarding.jar")
//    }

//    docker {
//        jreVersion.set(JavaVersion.VERSION_21)
//        localImageName.set("waltid/kyb-onboarding")
//        imageTag.set("1.0.0-SNAPSHOT")
//        portMappings.set(listOf(
//            io.ktor.plugin.features.DockerPortMapping(
//                3000,
//                3000,
//                io.ktor.plugin.features.DockerPortMappingProtocol.TCP
//            )
//        ))
//
//        externalRegistry.set(
//            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
//                appName = provider { "kyb-onboarding" },
//                username = provider { "waltid" },
//                password = provider { "If87v%u%cVBB" }
//            )
//        )
//    }
//}