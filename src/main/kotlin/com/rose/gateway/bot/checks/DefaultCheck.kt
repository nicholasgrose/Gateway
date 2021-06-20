package com.rose.gateway.bot.checks

import com.kotlindiscord.kord.extensions.checks.channelFor
import com.kotlindiscord.kord.extensions.checks.isBot
import com.rose.gateway.bot.DiscordBot
import dev.kord.core.event.Event

object DefaultCheck {
    suspend fun defaultCheck(event: Event): Boolean {
        val channelBehaviour = channelFor(event) ?: return false
        val channel = channelBehaviour.asChannelOrNull() ?: return false

        return when {
            isBot(event) -> false
            !DiscordBot.getBotChannels().contains(channel) -> false
            else -> true
        }
    }
}
