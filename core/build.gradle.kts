plugins {
    id("gateway.conventions.kotlin")
}

dependencies {
    implementation(coreLibs.bundles.hoplite)
    implementation(coreLibs.kaml)
}
