package xyz.rose.gateway.minecraft.plugin

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.minecraft.capability.MinecraftWhitelistReadCapability
import xyz.rose.gateway.minecraft.capability.MinecraftWhitelistWriteCapability
import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.Registrar

/**
 * Interface for Minecraft whitelist plugins.
 * This plugin provides functionality for managing the Minecraft whitelist.
 */
interface MinecraftWhitelistPlugin : Plugin, Registrar<Capability> {
    /**
     * The capability for reading the whitelist.
     */
    val readCapability: MinecraftWhitelistReadCapability

    /**
     * The capability for writing to the whitelist.
     */
    val writeCapability: MinecraftWhitelistWriteCapability
}
