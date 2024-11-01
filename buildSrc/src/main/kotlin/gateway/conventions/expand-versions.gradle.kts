package gateway.conventions

import gradle.kotlin.dsl.accessors._18b46bc62d2d24efa2e4fdac1d739e7a.processResources

/**
 * Extension for configuring the expand versions convention plugin
 *
 * @constructor Create an empty plugin extension
 */
interface ExpandVersionsPluginExtension {
    val filePattern: Property<String>
}

// Creating the extension and giving it a default value
val extension = project.extensions.create<ExpandVersionsPluginExtension>("files")
extension.filePattern.convention("")

tasks {
    processResources {
        filesMatching(extension.filePattern.get()) {
            expand(
                "version" to version,
            )
        }
    }
}
