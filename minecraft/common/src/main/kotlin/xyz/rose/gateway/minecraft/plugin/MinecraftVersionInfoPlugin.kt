package xyz.rose.gateway.minecraft.plugin

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.minecraft.capability.MinecraftVersionInfoReadCapability
import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.Registrar

/**
 * Interface for Minecraft version info plugins.
 * This plugin provides functionality for retrieving information about the Minecraft server version.
 */
interface MinecraftVersionInfoPlugin : Plugin, Registrar<Capability> {
    /**
     * The capability for reading version information.
     */
    val versionInfoReadCapability: MinecraftVersionInfoReadCapability
}
