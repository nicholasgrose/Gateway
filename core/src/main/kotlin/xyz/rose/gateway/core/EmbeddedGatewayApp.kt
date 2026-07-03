package xyz.rose.gateway.core

import io.github.oshai.kotlinlogging.KLogger
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

    override fun start() {
        logger.info { "Starting Gateway..." }

        val koin = getKoin()
        koin.loadModules(platformModules)

        koin.getAll<Platform>().forEach { it.connect() }
        koin.getAll<Capability>().forEach {
            if (it is Capability.Enableable) {
                it.onEnable()
            }
        }
        koin.getAll<GatewayPlugin>().forEach { it.onEnable() }

        logger.info { "Gateway started!" }
    }

    override fun stop() {
        logger.info { "Stopping Gateway..." }

        val koin = getKoin()
        koin.getAll<GatewayPlugin>().forEach { it.onDisable() }
        koin.getAll<Capability>().forEach {
            if (it is Capability.Disableable) {
                it.onDisable()
            }
        }
        koin.getAll<Platform>().forEach { it.disconnect() }

        koin.unloadModules(platformModules)

        logger.info { "Gateway stopped!" }
    }
}
