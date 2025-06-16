import gateway.conventions.Expand_versions_gradle.ExpandVersionsPluginExtension

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
    maven("https://jitpack.io")
}

dependencies {
    include(project(":core"))
    include(project(":minecraft:common"))

    minecraft(fabricLibs.minecraft)
    // We need to do a little bit of manipulation because this uses a non-standard version format for v2
    // https://wiki.fabricmc.net/tutorial:mappings
    mappings(fabricLibs.fabric.yarn.get().toString())

    modImplementation(fabricLibs.bundles.fabric)
}

extensions.getByType<ExpandVersionsPluginExtension>().filePattern.set("fabric.mod.json")

tasks {
    runClient {
        dependsOn("setupDevEnv")
    }

    runServer {
        dependsOn("setupDevEnv")
    }
}
