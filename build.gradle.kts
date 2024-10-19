import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

val version: String by project
val group: String by project

val minecraftTestVersion: String by project

val ktlintVersion: String by project
val detektVersion: String by project

project.group = group
project.version = version

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.bundles.hoplite)
    implementation(libs.kaml)
    implementation(libs.kordex)
    implementation(libs.tegral)
}

ktlint {
    version.set(ktlintVersion)
}

detekt {
    config.from("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
    toolVersion = detektVersion
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
            apiVersion = KotlinVersion.KOTLIN_2_0
            languageVersion = KotlinVersion.KOTLIN_2_0
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

    build {
        dependsOn(shadowJar)
    }

    runServer {
        this.minecraftVersion(minecraftTestVersion)
    }

    detekt.configure {
        mustRunAfter(ktlintFormat)
    }

    create("runChecks") {
        dependsOn(ktlintFormat, detekt)
    }

    clean {
        delete("build")
    }
}
