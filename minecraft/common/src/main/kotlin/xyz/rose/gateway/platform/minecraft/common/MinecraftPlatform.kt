package xyz.rose.gateway.platform.minecraft.common

import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.GatewayPlatformDefinitionProvider
import xyz.rose.gateway.platform.minecraft.common.config.MinecraftConfigSchema

/**
 * The entry point for the Minecraft platform's connection logic.
 *
 * @constructor Create a new Minecraft platform
 */
class MinecraftPlatform : Platform {
    companion object : GatewayPlatformDefinitionProvider {
        override fun definition() = GatewayPlatformDefinition(
            schema = MinecraftConfigSchema(),
            provider = { MinecraftPlatformProvider() }
        )
    }

    override fun connect() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }
}
