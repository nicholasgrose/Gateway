//import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
//    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.shadow)
//    alias(libs.plugins.dotenv)
//    alias(libs.plugins.kover)
//    alias(libs.plugins.detekt)
//    alias(libs.plugins.qodana)
//    alias(libs.plugins.changelog)
}

val version: String by project
val group: String by project

val jvmTargetVersion: String by project
val kotlinTargetVersion: String by project
val minecraftTestVersion: String by project

project.group = group
project.version = version

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

//dependencies {
//    testImplementation(kotlin("test"))
//
//    detektPlugins(libs.detekt.formatting)
//}
//
//detekt {
//    buildUponDefaultConfig = true
//    config.from("detekt.yaml")
//}
//
//kover {
//    reports {
//        filters {
//            excludes {
//                // This package is automatically generated
//                packages("gateway.i18n")
//            }
//        }
//
//        total {
//            xml {
//                onCheck = true
//            }
//        }
//    }
//}
//
//tasks {
//    compileKotlin {
//        compilerOptions {
//            jvmTarget = JvmTarget.fromTarget(jvmTargetVersion)
//        }
//    }
//
//    processResources {
//        filesMatching("plugin.yml") {
//            expand(
//                "version" to version,
//            )
//        }
//    }
//
//    shadowJar {
//        archiveBaseName = rootProject.name
//        archiveClassifier = ""
//        archiveVersion = rootProject.version.toString()
//        mergeServiceFiles()
//    }
//
//    test {
//        useJUnitPlatform()
//    }
//}
