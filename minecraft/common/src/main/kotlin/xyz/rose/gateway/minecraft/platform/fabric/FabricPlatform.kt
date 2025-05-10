package xyz.rose.gateway.minecraft.platform.fabric

import xyz.rose.gateway.minecraft.config.MinecraftConfig
import xyz.rose.gateway.minecraft.impl.AbstractMinecraftPlatform

/**
 * Implementation of the MinecraftPlatform interface for Fabric.
 * This class provides functionality specific to the Fabric Minecraft server implementation.
 *
 * @property config The Minecraft platform configuration.
 */
class FabricPlatform(
    config: MinecraftConfig
) : AbstractMinecraftPlatform(config) {
    /**
     * Initializes the Fabric platform.
     * This method should be called when the Fabric mod is initialized.
     */
    fun initialize() {
        // In a real implementation, this would initialize the Fabric platform
        println("Initializing Fabric platform")
    }

    /**
     * Shuts down the Fabric platform.
     * This method should be called when the Fabric mod is unloaded.
     */
    fun shutdown() {
        // In a real implementation, this would shut down the Fabric platform
        println("Shutting down Fabric platform")
    }
}
