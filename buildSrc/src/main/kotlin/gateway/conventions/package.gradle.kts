package gateway.conventions

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar

plugins {
    id("com.gradleup.shadow")
}

tasks {
    // In the future, it may be worth considering Shadow's "relocate()" feature to prevent possible namespace clashes.
    shadowJar {
        archiveBaseName = project.name
        archiveClassifier = ""
        archiveVersion = project.version.toString()
        mergeServiceFiles()
        minimize()
    }
}
