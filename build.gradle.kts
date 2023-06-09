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
}

val paperVersion = "1.19.4-R0.1-SNAPSHOT"
val lampVersion = "3.1.5"
val vaultVersion = "1.7"
val mccoroutineVersion = "2.11.0"
val kotlinxcoroutineVersion = "1.7.0-RC"
val cloudVersion = "1.7.1"

dependencies {
    compileOnly("io.papermc.paper", "paper-api", paperVersion)

    library(kotlin("stdlib"))

    compileOnly("com.github.MilkBowl", "VaultAPI", vaultVersion)

    implementation("cloud.commandframework", "cloud-core", cloudVersion)
    implementation("cloud.commandframework", "cloud-kotlin-extensions", cloudVersion)
    implementation("cloud.commandframework", "cloud-paper", cloudVersion)
    implementation("cloud.commandframework", "cloud-annotations", cloudVersion)
    implementation("cloud.commandframework", "cloud-kotlin-coroutines-annotations", cloudVersion)
    implementation("cloud.commandframework", "cloud-kotlin-coroutines", cloudVersion)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinxcoroutineVersion)

    implementation("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-api", mccoroutineVersion)
    implementation("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-core", mccoroutineVersion)

    implementation("org.json:json:20230227")

    implementation("org.apache.commons:commons-io:1.3.2")
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
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
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