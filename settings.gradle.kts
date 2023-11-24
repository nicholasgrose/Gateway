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
