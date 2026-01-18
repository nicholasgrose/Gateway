plugins {
    `kotlin-dsl`
}

dependencies {
    // The following are, primarily, to work around the linked issue for a lack of support for version catalogs
    // in pre-compiled plugin scripts.
    // https://github.com/gradle/gradle/issues/15383#issuecomment-1855984127
    implementation(libs.plugin.kotlin.jvm)
    implementation(libs.plugin.kotlin.serialization)
    implementation(libs.plugin.kover)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.qodana)
    implementation(libs.plugin.shadow)

    // https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    // An example of a workaround for https://github.com/FabricMC/fabric-loom/issues/1020:
    // constraints { implementation("com.google.code.gson:gson:2.10.1") }
    // The above plugin version workaround can introduce issues like this,
    // where versions mismatch due to the inclusion of the plugins as libraries rather than pure plugins.
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}
