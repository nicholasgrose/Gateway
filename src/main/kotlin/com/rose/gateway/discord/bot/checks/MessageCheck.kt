package com.rose.gateway.discord.bot.checks

import com.kotlindiscord.kord.extensions.checks.channelFor
import com.kotlindiscord.kord.extensions.checks.messageFor
import com.kotlindiscord.kord.extensions.checks.types.Check
import com.rose.gateway.discord.bot.DiscordBot
import dev.kord.core.Kord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides event checks for individual Discord messages
 *
 * @see Check
 */
object MessageCheck : KoinComponent {
    private val bot: DiscordBot by inject()

    /**
     * Check that checks that the event is in a valid bot channel
     */
    val isValidBotChannel: Check<*> = {
        val channelBehaviour = channelFor(event)
        val channel = channelBehaviour?.asChannelOrNull()

        if (bot.context.botChannels.contains(channel)) pass()
        else fail("Channel is not configured bot channel.")
    }

    /**
     * Check that checks that the message was not sent by the bot, itself
     */
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
