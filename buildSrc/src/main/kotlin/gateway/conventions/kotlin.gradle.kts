package gateway.conventions

import gateway.libs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlinx.kover")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.qodana")
}

project.group = "com.rose.gateway"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    detektPlugins(libs.detekt.formatting)

    testImplementation(kotlin("test"))
}

detekt {
    buildUponDefaultConfig = true
    config.from("detekt.yaml")
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
    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    test {
        useJUnitPlatform()
    }
}
