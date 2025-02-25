import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.resource.factory)
}
group = "dev.nikomaru"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://plugins.gradle.org/m2/")
    maven("https://repo.incendo.org/content/repositories/snapshots")
    maven("https://repo.codemc.io/repository/maven-public/")
}


dependencies {
    compileOnly(libs.paper.api)

    implementation(libs.bundles.commands)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.ktor.client)

    implementation(libs.kotlin.reflect)

    implementation(libs.commons.io)
    implementation(libs.snakeyaml)

    implementation(libs.koin.core)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mock.bukkit)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.bundles.koin.test)

    testImplementation(libs.bundles.ktor.client)

    testImplementation(libs.commons.io)
}

kotlin {
    jvmToolchain {
        (this).languageVersion.set(JavaLanguageVersion.of(21))
    }
    jvmToolchain(21)
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
        compilerOptions.javaParameters = true
        compilerOptions.languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
    compileTestKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
    build {
        dependsOn(shadowJar)
    }
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
    runServer {
        minecraftVersion("1.21")
        val plugins = runPaper.downloadPluginsSpec {}
        downloadPlugins{
            downloadPlugins.from(plugins)
        }
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

sourceSets.main {
    resourceFactory {
        bukkitPluginYaml {
            name = "MinecraftPluginManager"
            version = "miencraft_plugin_version"
            website = "https://github.com/Nlkomaru/MinecraftPluginManager"
            main = "$group.minecraftpluginmanager.MinecraftPluginManager"
            apiVersion = "1.20"
            libraries = libs.bundles.coroutines.asString()
            softDepend = listOf()
        }
    }
}

fun Provider<MinimalExternalModuleDependency>.asString(): String {
    val dependency = this.get()
    return dependency.module.toString() + ":" + dependency.versionConstraint.toString()
}

fun Provider<ExternalModuleDependencyBundle>.asString(): List<String> {
    return this.get().map { dependency ->
        "${dependency.group}:${dependency.name}:${dependency.version}"
    }
}