import gateway.conventions.Expand_versions_gradle.ExpandVersionsPluginExtension

plugins {
    id("gateway.conventions.kotlin")
    id("gateway.conventions.expand-versions")
    id("gateway.conventions.dev")
    id("gateway.conventions.discord")
    id("gateway.conventions.package")
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
    maven("https://jitpack.io")
}

dependencies {
    include(project(":core"))
    include(project(":minecraft:common"))

    minecraft(fabricLibs.minecraft)

    implementation(fabricLibs.bundles.fabric)

    implementation(fabricLibs.adventure.platform.fabric)
}

extensions.getByType<ExpandVersionsPluginExtension>().filePattern.set("fabric.mod.json")

tasks {
    runClient {
        dependsOn("setupDevEnv")
    }

    runServer {
        dependsOn("setupDevEnv")
    }

    java {
        withSourcesJar()
    }
}
