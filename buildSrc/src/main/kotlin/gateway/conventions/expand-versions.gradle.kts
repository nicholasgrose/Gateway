package gateway.conventions

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
    getByName<ProcessResources>("processResources") {
        filesMatching(extension.filePattern.get()) {
            expand(
                "version" to version,
            )
        }
    }
}
