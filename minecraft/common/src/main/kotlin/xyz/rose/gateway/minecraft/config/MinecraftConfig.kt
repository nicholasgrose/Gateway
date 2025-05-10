package xyz.rose.gateway.minecraft.config

import xyz.rose.gateway.config.GatewayPlatformConfig

/**
 * Configuration for the Minecraft platform.
 */
interface MinecraftConfig : GatewayPlatformConfig {
    /**
     * Color configuration for Minecraft messages.
     */
    val colors: MinecraftColorConfig
}

/**
 * Color configuration for Minecraft messages.
 *
 * @property primary Primary color for messages.
 * @property secondary Secondary color for messages.
 * @property tertiary Tertiary color for messages.
 * @property warning Warning color for messages.
 */
interface MinecraftColorConfig {
    val primary: String
    val secondary: String
    val tertiary: String
    val warning: String
}
