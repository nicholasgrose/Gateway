package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
import org.koin.core.scope.Scope
import xyz.rose.gateway.core.config.CombinedGatewayConfig
import xyz.rose.gateway.core.config.GatewayConfig
import xyz.rose.gateway.core.config.GatewayConfigSource
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition

/**
 * The Gateway environment for an embedded Gateway instance
 *
 * @property logger The logger to use
 * @property platforms The platform definitions to use
 * @property source The config source to use
 * @constructor Create a new embedded Gateway environment
 */
data class EmbeddedGatewayEnvironment(
    override val logger: KLogger,
    override val platforms: Collection<GatewayPlatformDefinition<*>>,
    override val source: GatewayConfigSource<Map<String, Any>>
) : GatewayEnvironment {
    override val appProvider: Scope.() -> GatewayApp = { EmbeddedGatewayApp(get()) }
    override val configProvider: Scope.() -> GatewayConfig = { CombinedGatewayConfig(get(), get(), get()) }
}
