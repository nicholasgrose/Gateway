package com.rose.gateway.discord.bot.checks

import com.kotlindiscord.kord.extensions.checks.channelFor
import com.kotlindiscord.kord.extensions.checks.messageFor
import com.kotlindiscord.kord.extensions.checks.types.Check
import com.rose.gateway.discord.bot.DiscordBot
import dev.kord.core.Kord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object MessageCheck : KoinComponent {
    val bot: DiscordBot by inject()

    val isConfiguredBotChannel: Check<*> = {
        val channelBehaviour = channelFor(event)
        val channel = channelBehaviour?.asChannelOrNull()

        if (bot.botChannels.contains(channel)) pass()
        else fail("Channel is not configured bot channel.")
    }

    val notSelf: Check<*> = {
        val kordClient = getKoin().get<Kord>()
        val author = messageFor(event)?.asMessageOrNull()?.author

        when {
            author == null -> fail("No author found.")
            author.id == kordClient.selfId -> fail("Is self.")
            else -> pass()
        }
    }
}
