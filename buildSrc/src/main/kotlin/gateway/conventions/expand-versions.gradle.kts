package gateway.conventions

plugins {
    java
}

/**
 * Extension for configuring the version expanding convention plugin
 *
 * @constructor Create an empty plugin extension
 */
interface ExpandVersionsPluginExtension {
    /**
     * The Ant-style pattern of the file to run the expansion against
     * @see CopySpec.filesMatching
     */
    val filePattern: Property<String>
}

version = project.property("version") as String

// Creating the extension and giving it a default value
val extension = project.extensions.create<ExpandVersionsPluginExtension>("files")
extension.filePattern.convention("")

// afterEvaluate() is not the best to use, but, unfortunately, we can't get around needing to use it here.
// filesMatching() gets evaluated at build time, and it doesn't accept Property<String>.
// That means that this block would run before any build script we run this in, and filePattern will always be blank
// since that script won't have been able to set the property at the time this executes.
// Once we have a viable alternative to passing a constant string into fileMatching(), we can make this better.
project.afterEvaluate {
    tasks {
        processResources {
            // The usage of duplicateStrategy and with() here is a workaround for IntelliJ giving annoying warnings.
            // https://youtrack.jetbrains.com/issue/IDEA-296490
            // We can make this a bit cleaner once this is fixed.
            duplicatesStrategy = DuplicatesStrategy.INCLUDE

            with(copySpec {
                from(project.sourceSets.main.get().resources.srcDirs)

                filesMatching(extension.filePattern.get()) {
                    expand("version" to version)
                }
            })
        }
    }
}
