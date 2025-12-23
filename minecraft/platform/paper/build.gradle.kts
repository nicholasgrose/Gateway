import gateway.conventions.Expand_versions_gradle.ExpandVersionsPluginExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("gateway.conventions.kotlin")
    id("gateway.conventions.expand-versions")
    id("gateway.conventions.dev")
    alias(paperLibs.plugins.run.paper)
}

val minecraftVersion: String by project

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(paperLibs.paper.api)
    implementation(project(":core"))
    implementation(project(":discord"))
    implementation(project(":minecraft:common"))
}

extensions.getByType<ExpandVersionsPluginExtension>().filePattern.set("plugin.yml")

tasks {
    runServer {
        dependsOn("setupDevEnv")

        version = minecraftVersion
    }
}
