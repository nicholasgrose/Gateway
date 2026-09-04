package xyz.rose.gateway.platform.minecraft.common

import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.GatewayPlatformDefinitionProvider
import xyz.rose.gateway.core.platform.PlatformInfo
import xyz.rose.gateway.core.platform.PlatformType
import xyz.rose.gateway.platform.minecraft.common.config.MinecraftConfigSchema

/**
 * The entry point for the Minecraft platform's connection logic.
 */
class MinecraftPlatform : Platform {
    companion object : GatewayPlatformDefinitionProvider {
        override fun definition() = GatewayPlatformDefinition(
            uid = "minecraft",
            type = PlatformType.MINECRAFT,
            schema = MinecraftConfigSchema(),
            provider = MinecraftPlatformProvider()
        )
    }

    override val id: String = "minecraft"

    override val info: PlatformInfo = PlatformInfo(
        name = "Minecraft",
        version = "1.0.0"
    )

    override suspend fun connect() {
        // TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        // TODO("Not yet implemented")
    }
}
