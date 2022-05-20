package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.SurrogateBasedSerializer
import com.rose.gateway.configuration.markers.SurrogateConverter
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
