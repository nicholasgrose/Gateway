import gateway.conventions.Expand_versions_gradle.ExpandVersionsPluginExtension

plugins {
    id("gateway.conventions.kotlin")
    id("gateway.conventions.expand-versions")
    id("gateway.conventions.dev")
    id("gateway.conventions.discord")
    id("gateway.conventions.package")
    alias(paperLibs.plugins.run.paper)
}

val minecraftVersion = project.findProperty("versions.minecraft")?.toString()

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
