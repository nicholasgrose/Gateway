package com.rose.gateway.config.schema

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.chatExtensionEnabled
import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.discord.bot.extensions.chat.ChatExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Config options for the "chat extension".
 *
 * @constructor Creates a "chat config" with the provided data.
 *
 * @param enabled Whether the extension is enabled.
 */
@Serializable(with = ChatConfigSerializer::class)
class ChatConfig(
    enabled: Boolean
) : CommonExtensionConfig(enabled, ChatExtension.extensionName()) {
    companion object : KoinComponent {
        val config: PluginConfig by inject()

        /**
         * Runs some code if the chat extension is enabled.
         *
         * @param code The code block to execute.
         * @receiver This function.
         */
        fun ifEnabled(code: () -> Unit) {
            if (config.chatExtensionEnabled()) {
                code()
            }
        }
    }
}

/**
 * Surrogate for serialization of [ChatConfig].
 *
 * @property enabled Whether the extension is enabled.
 * @constructor Create a "chat config" surrogate with the provided data.
 *
 * @see ChatConfig
 * @see ChatConfigSerializer
 */
@Serializable
data class ChatConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<ChatConfig, ChatConfigSurrogate> {
        override fun fromBase(base: ChatConfig): ChatConfigSurrogate = ChatConfigSurrogate(base.enabled)

        override fun toBase(surrogate: ChatConfigSurrogate): ChatConfig = ChatConfig(surrogate.enabled)
    }
}

/**
 * Serializer for [ChatConfig].
 *
 * @see ChatConfig
 * @see ChatConfigSurrogate
 */
object ChatConfigSerializer :
    SurrogateBasedSerializer<ChatConfig, ChatConfigSurrogate>(ChatConfigSurrogate.serializer(), ChatConfigSurrogate)
