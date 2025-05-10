pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        maven("https://maven.fabricmc.net/")
        maven("https://repo.jpenilla.xyz/snapshots/")

        maven("https://snapshots-repo.kordex.dev")
        maven("https://releases-repo.kordex.dev")
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("commonLibs") {
            from(files("common/libs.versions.toml"))
        }

        create("discordLibs") {
            from(files("discord/libs.versions.toml"))
        }

        create("minecraftCommonLibs") {
            from(files("minecraft/common/libs.versions.toml"))
        }

        create("fabricLibs") {
            from(files("minecraft/platform/fabric/libs.versions.toml"))
        }

        create("paperLibs") {
            from(files("minecraft/platform/paper/libs.versions.toml"))
        }
    }
}



rootProject.name = "gateway"
listOf(
    "common",
    "bridge",
    "discord",
    "minecraft:common",
    "minecraft:platform:fabric",
    "minecraft:platform:paper",
).forEach(::include)
