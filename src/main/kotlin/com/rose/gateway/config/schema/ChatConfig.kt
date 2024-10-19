package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.discord.bot.extensions.chat.ChatExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable

/**
 * Config options for the "chat extension"
 *
 * @property showRoleColor Whether Discord role colors are shown
 * @constructor Creates a "chat config" with the provided data
 *
 * @param enabled Whether the extension is enabled
 */
@Serializable(with = ChatConfigSerializer::class)
class ChatConfig(
    enabled: Boolean,
    @ConfigItem("Whether Discord role colors are shown in Minecraft chat") val showRoleColor: Boolean,
) : CommonExtensionConfig(enabled, ChatExtension.extensionName())

/**
 * Surrogate for serialization of [ChatConfig]
 *
 * @property enabled Whether the extension is enabled
 * @property showRoleColor Whether Discord role colors are shown
 * @constructor Create a "chat config" surrogate with the provided data
 *
 * @see ChatConfig
 * @see ChatConfigSerializer
 */
@Serializable
data class ChatConfigSurrogate(val enabled: Boolean, val showRoleColor: Boolean) {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object : SurrogateConverter<ChatConfig, ChatConfigSurrogate> {
        override fun fromBase(base: ChatConfig): ChatConfigSurrogate =
            ChatConfigSurrogate(
                base.enabled,
                base.showRoleColor,
            )

        override fun toBase(surrogate: ChatConfigSurrogate): ChatConfig =
            ChatConfig(
                surrogate.enabled,
                surrogate.showRoleColor,
            )
    }
}

/**
 * Serializer for [ChatConfig]
 *
 * @see ChatConfig
 * @see ChatConfigSurrogate
 */
object ChatConfigSerializer :
    SurrogateBasedSerializer<ChatConfig, ChatConfigSurrogate>(ChatConfigSurrogate.serializer(), ChatConfigSurrogate)
