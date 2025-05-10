package xyz.rose.gateway.minecraft.config.impl

import xyz.rose.gateway.minecraft.config.MinecraftColorConfig
import xyz.rose.gateway.minecraft.config.MinecraftConfig

/**
 * Implementation of the MinecraftConfig interface.
 *
 * @property colors Color configuration for Minecraft messages.
 */
data class MinecraftConfigImpl(
    override val colors: MinecraftColorConfig = MinecraftColorConfigImpl()
) : MinecraftConfig

/**
 * Implementation of the MinecraftColorConfig interface.
 *
 * @property primary Primary color for messages.
 * @property secondary Secondary color for messages.
 * @property tertiary Tertiary color for messages.
 * @property warning Warning color for messages.
 */
data class MinecraftColorConfigImpl(
    override val primary: String = "#00FF00",   // Green
    override val secondary: String = "#FFFF00", // Yellow
    override val tertiary: String = "#00FFFF",  // Cyan
    override val warning: String = "#FF0000"    // Red
) : MinecraftColorConfig
