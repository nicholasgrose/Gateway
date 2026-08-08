package xyz.rose.gateway.platform.discord

import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.GatewayPlatformDefinitionProvider
import xyz.rose.gateway.core.platform.PlatformInfo
import xyz.rose.gateway.platform.discord.config.DiscordConfigSchema

/**
 * The entry point for the Discord platform's connection logic.
 */
class DiscordPlatform : Platform {
    companion object : GatewayPlatformDefinitionProvider {
        override fun definition() = GatewayPlatformDefinition(
            uid = "discord",
            type = "discord",
            schema = DiscordConfigSchema(),
            provider = { DiscordPlatformProvider() }
        )
    }

    override val id: String = "discord"

    override val info: PlatformInfo = PlatformInfo(
        name = "Discord",
        version = "1.0.0" // TODO: Get version from build or environment
    )

    override val capabilities: List<Capability> = emptyList() // TODO: Register Discord capabilities

    override suspend fun connect() {
        // TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        // TODO("Not yet implemented")
    }
}
