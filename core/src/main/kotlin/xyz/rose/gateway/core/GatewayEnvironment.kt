package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
import org.koin.core.parameter.ParametersHolder
import org.koin.core.scope.Scope
import xyz.rose.gateway.core.config.GatewayConfigBuilder
import xyz.rose.gateway.core.platform.GatewayPlatformProvider

/**
 * Represents the runtime environment for a Gateway application.
 *
 * This class encapsulates the necessary parts required to configure, log, and manage platform providers in Gateway.
 *
 * @property logger The logger instance used for logging within Gateway's runtime context.
 * @property appProvider A function that returns the Gateway app instance.
 * @property configBuilder The builder responsible for constructing the Gateway configuration.
 * @property providers A collection of platform providers that supply runtime modules for different platforms.
 * @constructor Create a new Gateway environment.
 */
data class GatewayEnvironment(
    val logger: KLogger,
    val appProvider: Scope.(ParametersHolder) -> GatewayApp,
    val configBuilder: GatewayConfigBuilder,
    val providers: Collection<GatewayPlatformProvider>
)
