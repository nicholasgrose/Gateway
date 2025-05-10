package xyz.rose.gateway.minecraft.plugin

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.minecraft.capability.MinecraftTpsReadCapability
import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.Registrar

/**
 * Interface for Minecraft TPS (Ticks Per Second) plugins.
 * This plugin provides functionality for monitoring server performance.
 */
interface MinecraftTpsPlugin : Plugin, Registrar<Capability> {
    /**
     * The capability for reading TPS information.
     */
    val tpsReadCapability: MinecraftTpsReadCapability
}
