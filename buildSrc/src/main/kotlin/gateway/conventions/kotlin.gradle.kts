package gateway.conventions

import gateway.libs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.bundles.kotlinx)
    implementation(libs.yamlkt)

    implementation(libs.kotlinLogging)

    detektPlugins(libs.detekt.formatting)

    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.koin.test)
    testImplementation(libs.mockk)
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
}
