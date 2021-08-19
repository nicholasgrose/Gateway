package com.rose.gateway.bot.message

import com.rose.gateway.GatewayPlugin
import dev.kord.core.behavior.channel.createMessage
import dev.kord.rest.builder.message.create.MessageCreateBuilder

class DiscordMessageSender(val plugin: GatewayPlugin) {
    suspend fun sendGameChatMessage(message: MessageCreateBuilder.() -> Unit) {
        sendToAllBotChannels(message)
    }

    private suspend fun sendToAllBotChannels(builder: MessageCreateBuilder.() -> Unit) {
        for (channel in plugin.discordBot.botChannels) {
            channel.createMessage(builder)
        }
    }
}
