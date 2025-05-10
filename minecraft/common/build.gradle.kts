plugins {
    id("gateway.conventions.kotlin")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    api(project(":common"))
    api(minecraftCommonLibs.bundles.adventure)

    testImplementation(kotlin("test"))
}
