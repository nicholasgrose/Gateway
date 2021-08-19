package com.rose.gateway.bot.checks

import com.kotlindiscord.kord.extensions.checks.channelFor
import com.kotlindiscord.kord.extensions.checks.types.Check
import com.rose.gateway.GatewayPlugin

class DefaultCheck(val plugin: GatewayPlugin) {
    val defaultCheck: Check<*> = {
        val channelBehaviour = channelFor(event)
        val channel = channelBehaviour?.asChannelOrNull()

        when {
            channel == null -> fail("Not in a channel.")
            plugin.discordBot.botChannels.contains(channel) -> fail("Channel not a bot channel.")
            else -> pass()
        }
    }
}
