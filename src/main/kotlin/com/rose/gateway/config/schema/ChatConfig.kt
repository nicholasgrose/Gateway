package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.config.markers.SurrogateBasedSerializer
import com.rose.gateway.config.markers.SurrogateConverter
import com.rose.gateway.discord.bot.extensions.chat.ChatExtension
import kotlinx.serialization.Serializable

@Serializable(with = ChatConfigSerializer::class)
class ChatConfig(
    enabled: Boolean
) : CommonExtensionConfig<ChatConfig>(enabled, ChatExtension.extensionName())

@Serializable
data class ChatConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<ChatConfig, ChatConfigSurrogate> {
        override fun fromBase(base: ChatConfig): ChatConfigSurrogate = ChatConfigSurrogate(base.enabled)

        override fun toBase(surrogate: ChatConfigSurrogate): ChatConfig = ChatConfig(surrogate.enabled)
    }
}

object ChatConfigSerializer :
    SurrogateBasedSerializer<ChatConfig, ChatConfigSurrogate>(ChatConfigSurrogate.serializer(), ChatConfigSurrogate)
