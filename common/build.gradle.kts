plugins {
    id("gateway.conventions.kotlin")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(commonLibs.bundles.hoplite)
    implementation(commonLibs.kaml)
    implementation(commonLibs.tegral)

    api(commonLibs.bundles.koin)
    api(commonLibs.bundles.kotlinx)
}
