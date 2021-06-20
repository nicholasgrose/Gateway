package com.rose.gateway.bot.message

import com.rose.gateway.bot.DiscordBot
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.MessageCreateBuilder

object MessageSender {
    suspend fun sendGameChatMessage(message: MessageCreateBuilder.() -> Unit) {
        sendToAllBotChannels(message)
    }

    private suspend fun sendToAllBotChannels(builder: MessageCreateBuilder.() -> Unit) {
        DiscordBot.getBotChannels().forEach {
            it.createMessage(builder)
        }
    }
}
