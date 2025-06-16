plugins {
    id("gateway.conventions.kotlin")
}

dependencies {
    implementation(project(":core"))
    implementation(minecraftCommonLibs.bundles.adventure)
}
