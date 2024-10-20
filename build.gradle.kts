import dev.kordex.gradle.plugins.kordex.DataCollection
import io.github.klahap.dotenv.DotEnvBuilder.Companion.dotEnv
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kordex)
    alias(libs.plugins.shadow)
    alias(libs.plugins.dotenv)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.detekt)
    alias(libs.plugins.qodana)
    alias(libs.plugins.changelog)
}

val version: String by project
val group: String by project

val jvmTargetVersion: String by project
val kotlinTargetVersion: String by project
val minecraftTestVersion: String by project

project.group = group
project.version = version

kordEx {
    jvmTarget = jvmTargetVersion.toInt()

    bot {
        // See https://docs.kordex.dev/data-collection.html
        dataCollection(DataCollection.Standard)
    }

    i18n {
        classPackage = "gateway.i18n"
        translationBundle = "discord.strings"
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    compileOnly(libs.paper.api)
    implementation(libs.bundles.hoplite)
    implementation(libs.kaml)
    implementation(libs.tegral)
}

detekt {
    buildUponDefaultConfig = true
    config.from("config/detekt/detekt.yml")
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(jvmTargetVersion)
        }
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "version" to version,
            )
        }
    }

    shadowJar {
        archiveBaseName = rootProject.name
        archiveClassifier = ""
        archiveVersion = rootProject.version.toString()
        mergeServiceFiles()
    }

    runServer {
        version = minecraftTestVersion

        // Adding environment variables useful for testing
        environment(
            dotEnv {
                addFile("$rootDir/dev.env")
            },
        )
    }
}
