package xyz.rose.gateway.core.platform

import org.koin.core.scope.Scope
import xyz.rose.gateway.core.config.ConfigSchema

/**
 * Defines a Gateway platform and how to instantiate it.
 *
 * @property type The type of platform this definition represents
 * @property uid A unique identifier for this platform (must be unique for this platform type)
 * @property schema The schema for the configuration of this platform.
 * @property provider The Koin provider for this platform.
 * @constructor Create a new Gateway platform definition
 */
data class GatewayPlatformDefinition<T : Any>(
    val type: PlatformType,
    val uid: PlatformUID,
    val schema: ConfigSchema<T>,
    val provider: PlatformProvider
)

/**
 * The type of platform this definition represents
 */
enum class PlatformType {
    DISCORD,
    MINECRAFT
}

/**
 * A unique identifier for a platform. This must be unique for its platform type.
 */
typealias PlatformUID = String
