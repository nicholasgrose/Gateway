package xyz.rose.gateway.platform.discord.message

import xyz.rose.gateway.core.capability.GatewayMessage
import xyz.rose.gateway.core.capability.MessageEnricher
import xyz.rose.gateway.core.capability.MessageMetadata
import xyz.rose.gateway.core.capability.chat.ChatMessage
import xyz.rose.gateway.core.platform.PlatformType
import kotlin.reflect.KClass

class GatewayChatMessageEnricher : MessageEnricher<ChatMessage> {
    override val dataType: KClass<ChatMessage> = ChatMessage::class

    override suspend fun enrich(message: GatewayMessage<ChatMessage>): MessageMetadata? {
        if (message.sourcePlatformType == PlatformType.DISCORD) return null

        return DiscordStuff("stuff")
    }
}

data class DiscordStuff(val stuff: String) : MessageMetadata
