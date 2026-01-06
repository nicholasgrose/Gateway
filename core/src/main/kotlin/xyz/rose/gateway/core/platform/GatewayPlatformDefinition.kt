package xyz.rose.gateway.core.platform

import org.koin.core.scope.Scope
import xyz.rose.gateway.core.config.GatewayConfigSchema

/**
 * Defines a Gateway platform and how to instantiate it.
 *
 * @property schema The schema for the configuration of this platform.
 * @property provider The Koin provider for this platform.
 * @constructor Create a new Gateway platform definition
 */
data class GatewayPlatformDefinition<T : Any>(
    val schema: GatewayConfigSchema<T>,
    val provider: Scope.() -> GatewayPlatformProvider
)
