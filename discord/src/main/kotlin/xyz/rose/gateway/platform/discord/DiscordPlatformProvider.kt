package xyz.rose.gateway.platform.discord

import org.koin.core.module.Module
import xyz.rose.gateway.core.config.GatewayConfig
import xyz.rose.gateway.core.config.GatewayConfigSchema
import xyz.rose.gateway.core.platform.GatewayPlatformProvider
import xyz.rose.gateway.platform.discord.config.DiscordConfigSchema

/**
 * Provides the necessary data to instantiate a Discord platform for Gateway.
 *
 * @constructor Create a new Discord platform provider
 */
class DiscordPlatformProvider : GatewayPlatformProvider {
    override fun getConfigSchema(): GatewayConfigSchema<*> = DiscordConfigSchema()

    override fun createRuntimeModule(config: GatewayConfig): Module {
        TODO("Not yet implemented")
    }
}
