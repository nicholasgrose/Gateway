plugins {
    id("gateway.conventions.kotlin")
}

dependencies {
    implementation(project(":core"))
    implementation(discordLibs.kord)
}
