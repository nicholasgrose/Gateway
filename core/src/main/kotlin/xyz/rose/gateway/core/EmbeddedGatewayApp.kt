package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.mp.KoinPlatform.getKoin
import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.PlatformProvider
import xyz.rose.gateway.core.plugin.GatewayPlugin

/**
 * A Gateway app that runs within the same JVM as the application.
 *
 * @constructor Create a new embedded Gateway app
 */
class EmbeddedGatewayApp(
    val logger: KLogger,
    val registry: GatewayRegistry
) : GatewayApp {
    override suspend fun start() = coroutineScope {
        logger.info { "Starting Gateway..." }

        // 1. Parallel connect
        registry.runtimes.map {
            async { it.value.platform.connect() }
        }.awaitAll()

        // 2. Enable capabilities
        registry.runtimes.flatMap { it.value.capabilities }
            .filterIsInstance<Capability.Enableable>()
            .forEach { it.onEnable() }

        // 3. Enable plugins
        registry.runtimes.flatMap { it.value.plugins }
            .forEach { it.onEnable() }

        logger.info { "Gateway started!" }
    }

    override suspend fun stop() = coroutineScope {
        logger.info { "Stopping Gateway..." }

        // 1. Disable plugins
        registry.runtimes.flatMap { it.value.plugins }
            .forEach { it.onDisable() }

        // 2. Disable capabilities
        registry.runtimes.flatMap { it.value.capabilities }
            .filterIsInstance<Capability.Disableable>()
            .forEach { it.onDisable() }

        // 3. Parallel disconnect
        registry.runtimes.map {
            async { it.value.platform.disconnect() }
        }.awaitAll()

        registry.close()

        logger.info { "Gateway stopped!" }
    }
}
