package xyz.rose.gateway

import xyz.rose.gateway.platform.Platform
import xyz.rose.gateway.registry.CapabilityRegistry
import xyz.rose.gateway.registry.PlatformRegistry
import xyz.rose.gateway.registry.PluginRegistry

/**
 * The main entry point for the Gateway application.
 * This interface provides methods for connecting and disconnecting platforms.
 */
interface Gateway {
    /**
     * The platform registry used by this Gateway instance.
     */
    val platformRegistry: PlatformRegistry

    /**
     * The plugin registry used by this Gateway instance.
     */
    val pluginRegistry: PluginRegistry

    /**
     * The capability registry used by this Gateway instance.
     */
    val capabilityRegistry: CapabilityRegistry

    /**
     * Connects a platform to this Gateway instance.
     *
     * @param platform The platform to connect.
     */
    fun connect(platform: Platform)

    /**
     * Disconnects a platform from this Gateway instance.
     *
     * @param platform The platform to disconnect.
     */
    fun disconnect(platform: Platform)
}
