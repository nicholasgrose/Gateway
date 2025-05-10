package xyz.rose.gateway.impl

import xyz.rose.gateway.Gateway
import xyz.rose.gateway.platform.Platform
import xyz.rose.gateway.registry.CapabilityRegistry
import xyz.rose.gateway.registry.PlatformRegistry
import xyz.rose.gateway.registry.PluginRegistry
import xyz.rose.gateway.registry.impl.CapabilityRegistryImpl
import xyz.rose.gateway.registry.impl.PlatformRegistryImpl
import xyz.rose.gateway.registry.impl.PluginRegistryImpl

/**
 * Implementation of the Gateway interface.
 */
class GatewayImpl : Gateway {
    override val platformRegistry: PlatformRegistry = PlatformRegistryImpl()
    override val pluginRegistry: PluginRegistry = PluginRegistryImpl()
    override val capabilityRegistry: CapabilityRegistry = CapabilityRegistryImpl()

    /**
     * Connects a platform to this Gateway instance.
     * This registers the platform in the platform registry.
     *
     * @param platform The platform to connect.
     */
    override fun connect(platform: Platform) {
        val registrationData = platformRegistry.registerWithData(platform)
        println("Connected platform: ${registrationData.platformId}")
    }

    /**
     * Disconnects a platform from this Gateway instance.
     * This deregisters the platform from the platform registry.
     *
     * @param platform The platform to disconnect.
     */
    override fun disconnect(platform: Platform) {
        platformRegistry.deregister(platform)
        println("Disconnected platform: ${platform.javaClass.simpleName}")
    }

    companion object {
        /**
         * Creates a new instance of the Gateway.
         *
         * @return A new Gateway instance.
         */
        fun create(): Gateway {
            return GatewayImpl()
        }
    }
}
