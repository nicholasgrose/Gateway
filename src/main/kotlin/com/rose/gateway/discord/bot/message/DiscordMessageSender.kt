package com.rose.gateway.discord.bot.message

import com.rose.gateway.discord.bot.DiscordBot
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides functions for sending messages in Discord.
 */
object DiscordMessageSender : KoinComponent {
    private val bot: DiscordBot by inject()

    /**
     * Sends a message to all valid channels.
     *
     * @param message The message to send.
     * @receiver The bot channels that the message is created in.
     */
    suspend fun sendGameChatMessage(message: MessageCreateBuilder.() -> Unit) {
        for (channel in bot.context.botChannels) {
            channel.createMessage(message)
        }
    }
}
