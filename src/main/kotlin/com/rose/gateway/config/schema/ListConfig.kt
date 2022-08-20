package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.discord.bot.extensions.list.ListExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable

/**
 * Config options for the "list extension".
 *
 * @constructor Creates a "list config" with the provided data.
 *
 * @param enabled Whether the extension is enabled.
 */
@Serializable(with = ListConfigSerializer::class)
class ListConfig(
    enabled: Boolean
) : CommonExtensionConfig(enabled, ListExtension.extensionName())

/**
 * Surrogate for serialization of [ListConfig].
 *
 * @property enabled Whether the extension is enabled.
 * @constructor Create a "list config" surrogate with the provided data.
 *
 * @see ListConfig
 * @see ListConfigSerializer
 */
@Serializable
data class ListConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<ListConfig, ListConfigSurrogate> {
        override fun fromBase(base: ListConfig): ListConfigSurrogate = ListConfigSurrogate(base.enabled)

        override fun toBase(surrogate: ListConfigSurrogate): ListConfig = ListConfig(surrogate.enabled)
    }
}

/**
 * Serializer for [ListConfig].
 *
 * @see ListConfig
 * @see ListConfigSurrogate
 */
object ListConfigSerializer :
    SurrogateBasedSerializer<ListConfig, ListConfigSurrogate>(ListConfigSurrogate.serializer(), ListConfigSurrogate)
