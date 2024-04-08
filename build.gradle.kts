plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.qodana)
}

val version: String by project
val group: String by project

val ktlintVersion: String by project
val minecraftVersion: String by project

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
    compileOnly(libs.papermc)
    implementation(libs.bundles.hoplite)
    implementation(libs.kaml)
    implementation(libs.kordExtensions)
    implementation(libs.tegralLexer)
    implementation(libs.bundles.ktor)
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
