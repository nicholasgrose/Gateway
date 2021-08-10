package com.rose.gateway.bot.checks

import com.kotlindiscord.kord.extensions.checks.channelFor
import com.kotlindiscord.kord.extensions.checks.types.Check
import com.rose.gateway.bot.DiscordBot

object DefaultCheck {
    val defaultCheck: Check<*> = {
        val channelBehaviour = channelFor(event)
        val channel = channelBehaviour?.asChannelOrNull()

        when {
            channel == null -> fail("Not in a channel.")
            !DiscordBot.getBotChannels().contains(channel) -> fail("Channel not a bot channel.")
            else -> pass()
        }
    }
}
