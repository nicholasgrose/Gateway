package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.module.Module
import org.koin.mp.KoinPlatform.getKoin
import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.PlatformProvider
import xyz.rose.gateway.core.plugin.GatewayPlugin

/**
 * A Gateway app that runs within the same JVM as the application.
 *
 * @constructor Create a new embedded Gateway app
 */
class EmbeddedGatewayApp(val logger: KLogger) : GatewayApp {
    /**
     * The platforms' modules that will be loaded into Koin
     */
    val platformModules: List<Module> = run {
        val koin = getKoin()
        val platformProviders = koin.getAll<PlatformProvider>()

        platformProviders.map { it.createRuntimeModule() }
    }

    override suspend fun start() = coroutineScope {
        logger.info { "Starting Gateway..." }

        val koin = getKoin()
        koin.loadModules(platformModules)

        val platforms = koin.getAll<Platform>()
        
        // 1. Parallel connect
        platforms.map { async { it.connect() } }.awaitAll()

        // 2. Enable capabilities
        koin.getAll<Capability>().forEach {
            if (it is Capability.Enableable) {
                it.onEnable()
            }
        }
        
        // 3. Enable plugins
        koin.getAll<GatewayPlugin>().forEach { it.onEnable() }

        logger.info { "Gateway started!" }
    }

    override suspend fun stop() = coroutineScope {
        logger.info { "Stopping Gateway..." }

        val koin = getKoin()
        
        // 1. Disable plugins
        koin.getAll<GatewayPlugin>().forEach { it.onDisable() }
        
        // 2. Disable capabilities
        koin.getAll<Capability>().forEach {
            if (it is Capability.Disableable) {
                it.onDisable()
            }
        }
        
        // 3. Parallel disconnect
        val platforms = koin.getAll<Platform>()
        platforms.map { async { it.disconnect() } }.awaitAll()

        koin.unloadModules(platformModules)

        logger.info { "Gateway stopped!" }
    }
}
