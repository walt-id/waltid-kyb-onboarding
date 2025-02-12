

val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.0.3"
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
}

repositories {
    mavenCentral()
    //maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    //maven("https://raw.githubusercontent.com/glureau/json-schema-serialization/mvn-repo")
    //maven("https://jitpack.io")
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

    implementation("org.litote.kmongo:kmongo-coroutine:${Versions.kmongoVersion}")
    implementation("org.mongodb:mongodb-driver-reactivestreams:${Versions.mongoDriverVersion}")
    implementation("org.mongodb:bson:${Versions.mongoDriverVersion}")
    implementation("org.mongodb:mongodb-driver-core:${Versions.mongoDriverVersion}")


    implementation("io.ktor:ktor-server-core-jvm:2.3.6")
    implementation("io.ktor:ktor-server-sessions-jvm:2.3.6")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.6")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.6")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.6")
    implementation("io.ktor:ktor-server-call-id-jvm:2.3.6")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.6")
    implementation("io.ktor:ktor-server-double-receive-jvm:2.3.6")
    implementation("io.ktor:ktor-server-cio-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auto-head-response:2.3.6")
    implementation("io.ktor:ktor-server-sessions-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.6")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.6")
    // Ktor client
    implementation("io.ktor:ktor-client-core-jvm:2.3.6")
    implementation("io.ktor:ktor-client-serialization-jvm:2.3.6")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.6")
    implementation("io.ktor:ktor-client-json-jvm:2.3.6")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.6")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.6")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.6")
    implementation("io.ktor:ktor-server-openapi:2.3.6")
    implementation("io.ktor:ktor-server-swagger:2.3.6")
    // Ktor external
    implementation("io.github.smiley4:ktor-swagger-ui:2.3.1")

    // Date/time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")


    // Logging
    implementation("io.github.oshai:kotlin-logging:5.1.0")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.slf4j:jul-to-slf4j:2.0.7")

    // Testing
    testImplementation(kotlin("test"))


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
