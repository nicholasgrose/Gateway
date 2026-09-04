package xyz.rose.gateway.platform.discord

import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.GatewayPlatformDefinitionProvider
import xyz.rose.gateway.core.platform.PlatformInfo
import xyz.rose.gateway.core.platform.PlatformType
import xyz.rose.gateway.platform.discord.config.DiscordConfigSchema

/**
 * The entry point for the Discord platform's connection logic.
 */
class DiscordPlatform : Platform {
    companion object : GatewayPlatformDefinitionProvider {
        override fun definition() = GatewayPlatformDefinition(
            uid = "discord",
            type = PlatformType.DISCORD,
            schema = DiscordConfigSchema(),
            provider = DiscordPlatformProvider()
        )
    }

    override val id: String = "discord"

    override val info: PlatformInfo = PlatformInfo(
        name = "Discord",
        version = "1.0.0"
    )

    override suspend fun connect() {
        // TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        // TODO("Not yet implemented")
    }
}
