package xyz.rose.gateway.platform

import xyz.rose.gateway.config.GatewayPlatformConfig
import xyz.rose.gateway.registry.RegistryItem

/**
 * Interface for platforms that can connect to Gateway.
 * Platforms are "the things that connect to Gateway" or "third parties".
 *
 * Examples of platforms:
 * - Minecraft
 * - Discord
 */
interface Platform : RegistryItem {
    fun loadConfig(): GatewayPlatformConfig
}
