import gateway.conventions.Expand_versions_gradle.ExpandVersionsPluginExtension
import org.gradle.kotlin.dsl.repositories

plugins {
    id("gateway.conventions.kotlin")
    id("gateway.conventions.expand-versions")
    id("gateway.conventions.dev")
    alias(fabricLibs.plugins.fabric.loom)
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("gateway") {
            sourceSet(sourceSets.main.get())
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    minecraft(fabricLibs.minecraft)
    mappings(fabricLibs.fabric.yarn)

    modImplementation(fabricLibs.bundles.fabric)
}

extensions.getByType<ExpandVersionsPluginExtension>().filePattern = "fabric.mod.json"

tasks {
    runClient {
        dependsOn("setupDevEnv")
    }

    runServer {
        dependsOn("setupDevEnv")
    }
}
