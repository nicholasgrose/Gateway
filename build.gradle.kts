plugins {
    // https://kotlinlang.org/
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    // https://github.com/johnrengelman/shadow
    id("com.github.johnrengelman.shadow") version "7.1.2"
    // https://github.com/jpenilla/run-paper
    id("xyz.jpenilla.run-paper") version "1.0.6"
    // https://github.com/jlleitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    // https://detekt.dev/
    id("io.gitlab.arturbosch.detekt") version "1.20.0-RC1"
}

group = "com.rose"
version = "1.5.0"
val minecraftVersion = "1.18.2"

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
    // https://papermc.io/using-the-api#gradle
    compileOnly(group = "io.papermc.paper", name = "paper-api", version = "$minecraftVersion-R0.1-SNAPSHOT")
    // https://github.com/uchuhimo/konf
    implementation(group = "com.uchuhimo", name = "konf-yaml", version = "1.1.2")
    // https://kordex.kotlindiscord.com/
    implementation(
        group = "com.kotlindiscord.kord.extensions",
        name = "kord-extensions",
        version = "1.5.2-SNAPSHOT"
    )
    // https://github.com/utybo/Lixy
    implementation(group = "guru.zoroark.lixy", name = "lixy-jvm", version = "master-SNAPSHOT")
    // https://github.com/kittinunf/fuel
    implementation(group = "com.github.kittinunf.fuel", name = "fuel", version = "2.3.1")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
            apiVersion = "1.6"
            languageVersion = "1.6"
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
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("$minecraftVersion")
    }

    create("runChecks") {
        dependsOn(ktlintFormat, detekt)
    }

    clean {
        delete("build")
    }
}
