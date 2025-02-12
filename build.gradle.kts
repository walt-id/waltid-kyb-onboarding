

val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.0.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}
object Versions {

    const val WALTID_VERSION = "0.11.0"
    const val mongoDriverVersion = "4.9.0"  // Use the latest stable version
    const val kmongoVersion = "4.9.0"
}
group = "id.walt"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
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
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-default-headers")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-serialization-gson")
    implementation("org.litote.kmongo:kmongo-coroutine:${Versions.kmongoVersion}")
    implementation("org.mongodb:mongodb-driver-reactivestreams:${Versions.mongoDriverVersion}")
    implementation("org.mongodb:bson:${Versions.mongoDriverVersion}")
    implementation("org.mongodb:mongodb-driver-core:${Versions.mongoDriverVersion}")

    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")


    implementation("id.walt:waltid-service-commons:${Versions.WALTID_VERSION}")

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
