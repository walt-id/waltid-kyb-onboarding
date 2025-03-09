

val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "2.3.13"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    application
}


object Versions {

    const val WALTID_VERSION = "0.11.0"
    const val mongoDriverVersion = "4.9.0"  // Use the latest stable version
    const val kmongoVersion = "4.9.0"
}
group = "id.walt"
version = "0.0.1"

application {
    mainClass.set("id.walt.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()

    maven("https://maven.waltid.dev/releases")
    maven("https://maven.waltid.dev/snapshots")
    maven {
        url = uri("https://maven.waltid.dev/private/releases")
        configureCredentials()
    }
    maven {
        url = uri("https://maven.waltid.dev/private/snapshots")
        configureCredentials()
    }
}

dependencies {

    // DB
    //MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.2.0")
    implementation("org.mongodb:bson-kotlinx:5.2.0")

    // Ktor Server
    implementation("io.ktor:ktor-server-core-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.6")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.6")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.6")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.6")
    implementation("io.ktor:ktor-server-default-headers-jvm:2.3.6")
    implementation("io.ktor:ktor-server-openapi:2.3.6")
    implementation("io.ktor:ktor-server-swagger:2.3.6")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.6")
    implementation("io.ktor:ktor-serialization-gson-jvm:2.3.6")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.6")
    implementation("io.ktor:ktor-server-call-id-jvm:2.3.6")
    implementation("io.ktor:ktor-server-double-receive-jvm:2.3.6")
    implementation("io.ktor:ktor-server-cio-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.6")
    implementation("io.ktor:ktor-server-sessions-jvm:2.3.6")

    // Ktor Client
    implementation("io.ktor:ktor-client-core-jvm:2.3.6")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-client-serialization-jvm:2.3.6")
    implementation("io.ktor:ktor-client-json-jvm:2.3.6")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.6")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.6")

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

fun MavenArtifactRepository.configureCredentials() {
    val repoName = "MAVEN"
    val (username, password) = waltidPrivateCredentials(repoName)
    require(username.isNotEmpty()) { getMissingRepoCredentialsErrorMessage(repoName, "username") }
    require(password.isNotEmpty()) { getMissingRepoCredentialsErrorMessage(repoName, "password") }
    credentials {
        this.username = username
        this.password = password
    }
}
fun waltidPrivateCredentials(repoName:String): Pair<String, String> = let {
    val envUsername = System.getenv(repoName.uppercase() + "_USERNAME")
    val envPassword = System.getenv(repoName.uppercase() + "_PASSWORD")

    val usernameFile = File("$rootDir/secret-${repoName.lowercase()}-username.txt")
    val passwordFile = File("$rootDir/secret-${repoName.lowercase()}-password.txt")

    return Pair(
        envUsername ?: usernameFile.let { if (it.isFile) it.readLines().first() else "" },
        envPassword ?: passwordFile.let { if (it.isFile) it.readLines().first() else "" }
    )
}
 fun getMissingRepoCredentialsErrorMessage(repo: String, field: String) =
    """
        Missing $repo credentials.
        Expecting non-empty value in:
            - environment variable: ${repo.uppercase()}_${field.uppercase()}
            - or text file in the project root: secret-${repo.lowercase()}-${field.lowercase()}.txt
    """.trimIndent()
ktor {
    fatJar {
        archiveFileName.set("kyb-onboarding.jar")
    }

    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("waltid/kyb-onboarding")
        imageTag.set("1.0.0-SNAPSHOT")
        portMappings.set(listOf(
            io.ktor.plugin.features.DockerPortMapping(
                3000,
                3000,
                io.ktor.plugin.features.DockerPortMappingProtocol.TCP
            )
        ))

        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "kyb-onboarding" },
                username = provider { "waltid" },
                password = provider { "If87v%u%cVBB" }
            )
        )
    }
}