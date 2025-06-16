plugins {
    id("gateway.conventions.kotlin")
}

dependencies {
    implementation(coreLibs.bundles.hoplite)
    implementation(coreLibs.bundles.kotlinx)
    implementation(coreLibs.kaml)
    implementation(coreLibs.tegral)
}
