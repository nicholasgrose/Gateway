import dev.kordex.gradle.plugins.kordex.DataCollection
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kordex)
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.qodana)
}

val version: String by project
val group: String by project

val jvmTargetVersion: String by project
val kotlinTargetVersion: String by project
val minecraftTestVersion: String by project

val ktlintVersion: String by project
val detektVersion: String by project

project.group = group
project.version = version

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.kordex.dev/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.bundles.hoplite)
    implementation(libs.kaml)
    implementation(libs.tegral)
}

detekt {
    config.from("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    toolVersion = detektVersion
}

ktlint {
    version = ktlintVersion
}

kordEx {
    jvmTarget = jvmTargetVersion.toInt()

    bot {
        // See https://docs.kordex.dev/data-collection.html
        dataCollection(DataCollection.Standard)

        mainClass = "com.rose.gateway.discord.bot.DiscordBotKt"
    }
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(jvmTargetVersion)
            apiVersion = KotlinVersion.fromVersion(kotlinTargetVersion)
            languageVersion = KotlinVersion.fromVersion(kotlinTargetVersion)
        }
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "version" to version,
            )
        }
    }

    jar.configure {
        onlyIf { false }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set(rootProject.version.toString())
        mergeServiceFiles()
    }

    runServer {
        this.minecraftVersion(minecraftTestVersion)
    }

    detekt.configure {
        mustRunAfter(ktlintFormat)
    }

    create("runLints") {
        dependsOn(ktlintFormat, detekt)
    }

    clean {
        delete("build")
    }
}
