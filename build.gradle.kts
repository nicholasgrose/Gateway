plugins {
    // https://kotlinlang.org/
    kotlin("jvm") version "1.9.21"
    // https://kotlinlang.org/docs/serialization.html
    kotlin("plugin.serialization") version "1.9.21"
    // https://github.com/johnrengelman/shadow
    id("com.github.johnrengelman.shadow") version "8.1.1"
    // https://github.com/jpenilla/run-paper
    id("xyz.jpenilla.run-paper") version "2.2.2"
    // https://github.com/jlleitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    // https://detekt.dev/
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
}

val version: String by project
val group: String by project

val ktlintVersion: String by project

val minecraftVersion: String by project
val paperApiRevision: String by project
val kordexVersion: String by project
val tegralNiwenVersion: String by project
val kamlVersion: String by project
val ktorVersion: String by project
val hopliteVersion: String by project

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
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    // Paper plugin development API.
    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-$paperApiRevision-SNAPSHOT")
    // Config library that provides nice parsing error descriptions.
    implementation("com.sksamuel.hoplite:hoplite-core:$hopliteVersion")
    implementation("com.sksamuel.hoplite:hoplite-yaml:$hopliteVersion")
    // Library that supports loading and (more importantly) saving to YAML files.
    implementation("com.charleskorn.kaml:kaml:$kamlVersion")
    // Library for Discord bots.
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:$kordexVersion")
    // Lexer library that supports regex.
    implementation(tegralLibs.niwen.lexer)
}

ktlint {
    version.set(ktlintVersion)
}

detekt {
    config.from("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = jvmVersion
            apiVersion = kotlinApiVersion
            languageVersion = kotlinLanguageVersion
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
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
        val minecraftVersion: String by project

        this.minecraftVersion(minecraftVersion)
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
