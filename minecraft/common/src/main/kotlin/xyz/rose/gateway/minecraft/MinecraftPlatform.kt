package xyz.rose.gateway.minecraft

import xyz.rose.gateway.minecraft.config.MinecraftConfig
import xyz.rose.gateway.platform.Platform
import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.Registrar

/**
 * Interface for Minecraft platforms.
 * This is the base interface for all Minecraft platform implementations (Paper, Fabric, etc.).
 */
interface MinecraftPlatform : Platform, Registrar<Plugin> {
    /**
     * The Minecraft platform configuration.
     */
    val config: MinecraftConfig
}
