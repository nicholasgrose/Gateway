pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        maven("https://maven.fabricmc.net/")
        maven("https://repo.jpenilla.xyz/snapshots/")

        maven("https://snapshots.kord.dev")
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("coreLibs") {
            from(files("core/libs.versions.toml"))
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
    "core",
    "bridge",
    "discord",
    "minecraft:common",
    "minecraft:platform:fabric",
    "minecraft:platform:paper",
).forEach(::include)
