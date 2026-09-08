package gateway

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

/**
 * Alias for accessing the main version catalog
 */
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Safe accessor for libraries in the version catalog
 *
 * @param name The name of the library to access
 * @return The provider for the library
 */
fun VersionCatalog.library(name: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(name).orElseThrow { NoSuchElementException("Library $name not found in catalog") }

/**
 * Safe accessor for bundles in the version catalog
 *
 * @param name The name of the bundle to access
 * @return The provider for the bundle
 */
fun VersionCatalog.bundle(name: String): Provider<ExternalModuleDependencyBundle> =
    findBundle(name).orElseThrow { NoSuchElementException("Bundle $name not found in catalog") }
