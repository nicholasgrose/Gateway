plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
}

group = "com.rose"
version = "1.4.4"

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
    compileOnly(group = "io.papermc.paper", name = "paper-api", version = "1.18.1-R0.1-SNAPSHOT")
    implementation(group = "com.uchuhimo", name = "konf-yaml", version = "1.1.2")
    implementation(
        group = "com.kotlindiscord.kord.extensions",
        name = "kord-extensions",
        version = "1.5.1-SNAPSHOT"
    )
    implementation(group = "guru.zoroark.lixy", name = "lixy-jvm", version = "master-SNAPSHOT")
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
        minecraftVersion("1.18.1")
    }

    clean {
        delete("build")
    }
}
