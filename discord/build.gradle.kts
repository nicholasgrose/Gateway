import dev.kordex.gradle.plugins.kordex.DataCollection

plugins {
    id("gateway.conventions.kotlin")
    alias(discordLibs.plugins.kordex)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
}

kordEx {
    jvmTarget = 21

    bot {
        // See https://docs.kordex.dev/data-collection.html
        dataCollection(DataCollection.Standard)
    }

    i18n {
        classPackage = "gateway.i18n"
        translationBundle = "discord.strings"
    }
}

kover {
    reports {
        filters {
            excludes {
                // This package is automatically generated
                packages("gateway.i18n")
            }
        }
    }
}
