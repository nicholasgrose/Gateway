plugins {
    // https://kotlinlang.org/
    kotlin("jvm") version "1.6.20"
    // https://github.com/johnrengelman/shadow
    id("com.github.johnrengelman.shadow") version "7.1.2"
    // https://github.com/jpenilla/run-paper
    id("xyz.jpenilla.run-paper") version "1.0.6"
    // https://github.com/jlleitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    // https://detekt.dev/
    id("io.gitlab.arturbosch.detekt") version "1.20.0-RC2"
}

val version: String by project
val group: String by project

val ktlintVersion: String by project

val minecraftVersion: String by project
val paperApiRevision: String by project
val konfVersion: String by project
val kordexVersion: String by project
val lixyVersion: String by project
val fuelVersion: String by project

val jvmVersion: String by project
val kotlinLanguageVersion: String by project
val kotlinApiVersion: String by project

project.group = group
project.version = version

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "PaperMC"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "Kotlin Discord"
        url = uri("https://maven.kotlindiscord.com/repository/maven-public/")
    }
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly(
        group = "io.papermc.paper",
        name = "paper-api",
        version = "$minecraftVersion-$paperApiRevision-SNAPSHOT"
    )
    implementation(group = "com.uchuhimo", name = "konf-yaml", version = konfVersion)
    implementation(
        group = "com.kotlindiscord.kord.extensions",
        name = "kord-extensions",
        version = kordexVersion
    )
    implementation(group = "guru.zoroark.lixy", name = "lixy-jvm", version = lixyVersion)
    implementation(group = "com.github.kittinunf.fuel", name = "fuel", version = fuelVersion)
}

ktlint {
    version.set(ktlintVersion)
}

detekt {
    config = files("detekt-config.yml")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = jvmVersion
            apiVersion = kotlinApiVersion
            languageVersion = kotlinLanguageVersion
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "version" to version
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
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        val minecraftVersion: String by project

        this.minecraftVersion(minecraftVersion)
    }

    create("runChecks") {
        dependsOn(ktlintFormat, detekt)
    }

    clean {
        delete("build")
    }
}
