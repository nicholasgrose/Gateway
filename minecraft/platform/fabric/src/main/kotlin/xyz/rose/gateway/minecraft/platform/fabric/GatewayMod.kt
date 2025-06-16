package xyz.rose.gateway.minecraft.platform.fabric

import net.fabricmc.api.ModInitializer

/**
 * The mod entry point for the Gateway Fabric mod
 *
 * @constructor Create a new Fabric mod
 */
class GatewayMod : ModInitializer {
    override fun onInitialize() {
        println("Hello Fabric world!")
    }
}
