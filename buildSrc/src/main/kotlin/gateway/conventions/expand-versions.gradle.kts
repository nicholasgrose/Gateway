package gateway.conventions

/**
 * Extension for configuring the version expanding convention plugin
 *
 * @constructor Create an empty plugin extension
 */
interface ExpandVersionsPluginExtension {
    /**
     * The Regex pattern of the file to run the expansion against
     */
    val filePattern: Property<String>
}

version = project.property("version") as String

// Creating the extension and giving it a default value
val extension = project.extensions.create<ExpandVersionsPluginExtension>("files")
extension.filePattern.convention("")

tasks {
    getByName<ProcessResources>("processResources") {

        filesMatching("**") {
            if (name.matches(Regex(extension.filePattern.get()))) {
                expand(
                    "version" to version
                )
            }
        }
    }
}
