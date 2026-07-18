package xyz.rose.gateway.platform.discord

import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.GatewayPlatformDefinitionProvider
import xyz.rose.gateway.core.platform.PlatformType
import xyz.rose.gateway.platform.discord.config.DiscordConfigSchema

/**
 * The entry point for the Discord platform's connection logic.
 *
 * @constructor Create a new Discord platform
 */
class DiscordPlatform : Platform {
    companion object : GatewayPlatformDefinitionProvider {
        override fun definition() = GatewayPlatformDefinition(
            uid = "discord",
            type = PlatformType.Discord,
            schema = DiscordConfigSchema(),
            provider = { DiscordPlatformProvider() }
        )
    }

    override fun connect() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }
}
