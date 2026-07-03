package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
import org.koin.core.scope.Scope
import xyz.rose.gateway.core.config.Config
import xyz.rose.gateway.core.config.ConfigSource
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition


/**
 * Represents the runtime environment for a Gateway application.
 *
 * This class encapsulates the necessary parts required to configure, log, and manage platform providers in Gateway.
 *
 * @property logger The logger instance used for logging within Gateway's runtime context.
 * @property platforms The platform definitions that Gateway is to use.
 * @property source The config source to use for Gateway's configuration.
 * @property appProvider A function that returns the Gateway app to use.
 * @property configProvider A function that returns the Gateway config to use.
 * @constructor Create a new Gateway environment.
 */
interface GatewayEnvironment {
    val logger: KLogger
    val platforms: Collection<GatewayPlatformDefinition<*>>
    val source: ConfigSource<*>
    val appProvider: Scope.() -> GatewayApp
    val configProvider: Scope.() -> Config
}
