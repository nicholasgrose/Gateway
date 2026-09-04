package xyz.rose.gateway.platform.discord.message

import xyz.rose.gateway.core.capability.chat.ChatMessage
import xyz.rose.gateway.core.capability.chat.ChatSend

class DiscordMessageSender(val chatSender: ChatSend, val enricher: GatewayChatMessageEnricher) {
    suspend fun send(message: ChatMessage) {
        chatSender.send(message)
    }
}
