rootProject.name = "Gateway"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "Integrated Server"
            url = uri("https://repo.jpenilla.xyz/snapshots/")
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("tegralLibs") {
            from("guru.zoroark.tegral:tegral-catalog:0.0.4")
        }
    }
}
