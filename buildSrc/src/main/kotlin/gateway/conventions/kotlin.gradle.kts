package gateway.conventions

import gateway.bundle
import gateway.library
import gateway.libs

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.kover")
    id("dev.detekt")
    id("org.jetbrains.qodana")
}

project.group = "com.rose.gateway"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(platform(libs.library("koin-bom")))
    implementation(libs.library("koin-core"))

    implementation(libs.bundle("kotlinx"))
    implementation(libs.library("yamlkt"))

    implementation(libs.library("kotlinLogging"))

    detektPlugins(libs.library("detekt-ktlint"))
    detektPlugins(libs.library("detekt-junit"))

    testImplementation(kotlin("test"))
    testImplementation(libs.bundle("koin-test"))
    testImplementation(libs.library("mockk"))
}

kotlin {
    jvmToolchain(25)
}

detekt {
    buildUponDefaultConfig = true
    config.from(rootDir.path + "/detekt.yaml")
}

kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
        // Mockk does some byte manipulation to work. This fixes or hides the warning that generates in the console.
        jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }

    qodanaScan {
        arguments.addAll("--config", rootDir.path + "/qodana.yaml")
    }
}
