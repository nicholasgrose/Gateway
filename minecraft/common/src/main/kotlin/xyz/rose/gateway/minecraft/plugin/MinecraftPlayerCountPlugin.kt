package xyz.rose.gateway.minecraft.plugin

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.minecraft.capability.MinecraftOnlinePlayerReadCapability
import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.Registrar

/**
 * Interface for Minecraft player count plugins.
 * This plugin provides functionality for retrieving information about the number of online players.
 */
interface MinecraftPlayerCountPlugin : Plugin, Registrar<Capability> {
    /**
     * The capability for reading online player information.
     */
    val onlinePlayerReadCapability: MinecraftOnlinePlayerReadCapability
}
