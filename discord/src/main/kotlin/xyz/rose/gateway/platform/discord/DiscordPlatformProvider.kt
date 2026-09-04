package xyz.rose.gateway.platform.discord

import org.koin.core.module.Module
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.ScopeDSL
import xyz.rose.gateway.core.platform.PlatformProvider
import xyz.rose.gateway.platform.discord.message.DiscordMessageSender
import xyz.rose.gateway.platform.discord.message.GatewayChatMessageEnricher

/**
 * Provides the necessary data to instantiate a Discord platform for Gateway.
 *
 * @constructor Create a new Discord platform provider
 */
class DiscordPlatformProvider : PlatformProvider {
    override fun ScopeDSL.createRuntimeModule() {
        scopedOf(::GatewayChatMessageEnricher)
        scopedOf(::DiscordMessageSender)
    }
}
