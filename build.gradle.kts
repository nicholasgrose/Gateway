plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.changelog)
}

repositories {
    mavenCentral()
}

tasks {
//    shadowJar {
//        archiveBaseName = rootProject.name
//        archiveClassifier = ""
//        archiveVersion = rootProject.version.toString()
//        mergeServiceFiles()
//    }
}
