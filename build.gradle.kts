import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("java")
    kotlin("jvm") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    kotlin("plugin.serialization") version "1.8.10"
}
group = "dev.nikomaru"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://plugins.gradle.org/m2/")
    maven("https://repo.incendo.org/content/repositories/snapshots")
    maven("https://repo.codemc.io/repository/maven-public/")
}


dependencies {
    val paperVersion = "1.20.4-R0.1-SNAPSHOT"
    val mccoroutineVersion = "2.14.0"
    val lampVersion = "3.1.9"
    val koinVersion = "3.5.3"
    val coroutineVersion = "1.7.3"
    val serializationVersion = "1.6.2"
    val junitVersion = "5.10.1"
    val mockBukkitVersion = "3.68.0"
    val ktorVersion = "2.3.8"


    compileOnly("io.papermc.paper:paper-api:$paperVersion")

    library(kotlin("stdlib"))

    implementation("com.github.Revxrsal.Lamp:common:$lampVersion")
    implementation("com.github.Revxrsal.Lamp:bukkit:$lampVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    library("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:$mccoroutineVersion")
    library("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:$mccoroutineVersion")

    library("org.yaml:snakeyaml:2.2")

    implementation("io.insert-koin:koin-core:$koinVersion")

    library("io.ktor:ktor-client-core:$ktorVersion")
    library("io.ktor:ktor-client-cio:$ktorVersion")
    library("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    library("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    library("commons-io:commons-io:2.15.1")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:$mockBukkitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")

    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-cio:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    testImplementation("commons-io:commons-io:2.15.1")

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.javaParameters = true
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    build {
        dependsOn(shadowJar)
    }
    runServer {
        minecraftVersion("1.20.4")
    }
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
    test {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}


bukkit {
    name = "MinecraftPluginManager" // need to change
    version = "minecraft_plugin_version"
    website = "https://github.com/Nlkomaru/MinecraftPluginManager"  // need to change

    main = "$group.minecraftpluginmanager.MinecraftPluginManager"  // need to change

    apiVersion = "1.19"
    libraries = listOf("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.11.0",
        "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.11.0")
}