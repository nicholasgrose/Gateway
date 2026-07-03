package xyz.rose.gateway.platform.discord

import org.koin.core.module.Module
import xyz.rose.gateway.core.platform.PlatformProvider

/**
 * Provides the necessary data to instantiate a Discord platform for Gateway.
 *
 * @constructor Create a new Discord platform provider
 */
class DiscordPlatformProvider : PlatformProvider {
    override fun createRuntimeModule(): Module {
        TODO("Not yet implemented")
    }
}
