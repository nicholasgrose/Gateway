package xyz.rose.gateway.core

import org.koin.mp.KoinPlatform.getKoin
import xyz.rose.gateway.core.capability.GatewayCapability
import xyz.rose.gateway.core.platform.GatewayPlatform
import xyz.rose.gateway.core.plugin.GatewayPlugin

/**
 * A Gateway app that runs within the same JVM as the application.
 *
 * @constructor Create a new embedded Gateway app
 */
class EmbeddedGatewayApp : GatewayApp {
    override fun start() {
        val koin = getKoin()
        koin.getAll<GatewayPlatform>().forEach { it.connect() }
        koin.getAll<GatewayCapability>().forEach { it.onEnable() }
        koin.getAll<GatewayPlugin>().forEach { it.onEnable() }
    }

    override fun stop() {
        val koin = getKoin()
        koin.getAll<GatewayPlugin>().forEach { it.onDisable() }
        koin.getAll<GatewayCapability>().forEach { it.onDisable() }
        koin.getAll<GatewayPlatform>().forEach { it.disconnect() }
    }
}
