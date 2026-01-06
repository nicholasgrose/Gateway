package xyz.rose.gateway.platform.discord

import org.koin.core.module.Module
import xyz.rose.gateway.core.platform.GatewayPlatformProvider

/**
 * Provides the necessary data to instantiate a Discord platform for Gateway.
 *
 * @constructor Create a new Discord platform provider
 */
class DiscordPlatformProvider : GatewayPlatformProvider {
    override fun createRuntimeModule(): Module {
        TODO("Not yet implemented")
    }
}
