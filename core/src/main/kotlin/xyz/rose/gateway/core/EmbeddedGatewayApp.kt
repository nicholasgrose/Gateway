package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
import org.koin.core.module.Module
import org.koin.mp.KoinPlatform.getKoin
import xyz.rose.gateway.core.capability.GatewayCapability
import xyz.rose.gateway.core.platform.GatewayPlatform
import xyz.rose.gateway.core.platform.GatewayPlatformProvider
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
        val platformProviders = koin.getAll<GatewayPlatformProvider>()

        platformProviders.map { it.createRuntimeModule() }
    }

    override fun start() {
        logger.info { "Starting Gateway..." }

        val koin = getKoin()
        koin.loadModules(platformModules)

        koin.getAll<GatewayPlatform>().forEach { it.connect() }
        koin.getAll<GatewayCapability>().forEach { it.onEnable() }
        koin.getAll<GatewayPlugin>().forEach { it.onEnable() }

        logger.info { "Gateway started!" }
    }

    override fun stop() {
        logger.info { "Stopping Gateway..." }

        val koin = getKoin()
        koin.getAll<GatewayPlugin>().forEach { it.onDisable() }
        koin.getAll<GatewayCapability>().forEach { it.onDisable() }
        koin.getAll<GatewayPlatform>().forEach { it.disconnect() }

        koin.unloadModules(platformModules)

        logger.info { "Gateway stopped!" }
    }
}
