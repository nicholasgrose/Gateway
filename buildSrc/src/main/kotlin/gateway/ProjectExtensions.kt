package gateway

import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.accessors.dm.LibrariesForLibs

/**
 * Alias for accessing the main version catalog
 */
val Project.libs get() = the<LibrariesForLibs>()
