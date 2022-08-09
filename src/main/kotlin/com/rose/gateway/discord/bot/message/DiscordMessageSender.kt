package com.rose.gateway.discord.bot.message

import com.rose.gateway.discord.bot.DiscordBot
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DiscordMessageSender : KoinComponent {
    val bot: DiscordBot by inject()

    suspend fun sendGameChatMessage(message: MessageCreateBuilder.() -> Unit) {
        sendToAllBotChannels(message)
    }

    private suspend fun sendToAllBotChannels(builder: MessageCreateBuilder.() -> Unit) {
        for (channel in bot.botChannels) {
            channel.createMessage(builder)
        }
    }
}
