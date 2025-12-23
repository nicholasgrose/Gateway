import dev.kordex.gradle.plugins.kordex.DataCollection
import org.gradle.kotlin.dsl.i18n

plugins {
    id("gateway.conventions.kotlin")
    alias(discordLibs.plugins.kordex)
    alias(discordLibs.plugins.i18n)
}

dependencies {
    implementation(project(":core"))
}

kordEx {
    jvmTarget = 21

    bot {
        // See https://docs.kordex.dev/data-collection.html
        dataCollection(DataCollection.Standard)
    }

    ignoreIncompatibleKotlinVersion = true
}

i18n {
    bundle("discord.strings", "gateway.i18n")
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
