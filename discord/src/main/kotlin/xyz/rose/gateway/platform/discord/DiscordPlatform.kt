package xyz.rose.gateway.platform.discord

import xyz.rose.gateway.core.platform.GatewayPlatform
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.GatewayPlatformDefinitionProvider
import xyz.rose.gateway.platform.discord.config.DiscordConfigSchema

/**
 * The entry point for the Discord platform's connection logic.
 *
 * @constructor Create a new Discord platform
 */
class DiscordPlatform : GatewayPlatform {
    companion object : GatewayPlatformDefinitionProvider {
        override fun definition() = GatewayPlatformDefinition(
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
