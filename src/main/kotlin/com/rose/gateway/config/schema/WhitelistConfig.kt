package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.discord.bot.extensions.whitelist.WhitelistExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable

/**
 * Config options for the "whitelist extension".
 *
 * @constructor Creates a "whitelist config" with the provided data.
 *
 * @param enabled Whether the extension is enabled.
 */
@Serializable(with = WhitelistConfigSerializer::class)
class WhitelistConfig(
    enabled: Boolean
) : CommonExtensionConfig(enabled, WhitelistExtension.extensionName())

/**
 * Surrogate for serialization of [WhitelistConfig].
 *
 * @property enabled Whether the extension is enabled.
 * @constructor Create a "whitelist config" surrogate with the provided data.
 *
 * @see WhitelistConfig
 * @see WhitelistConfigSerializer
 */
@Serializable
data class WhitelistConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<WhitelistConfig, WhitelistConfigSurrogate> {
        override fun fromBase(base: WhitelistConfig): WhitelistConfigSurrogate = WhitelistConfigSurrogate(base.enabled)

        override fun toBase(surrogate: WhitelistConfigSurrogate): WhitelistConfig = WhitelistConfig(surrogate.enabled)
    }
}

/**
 * Serializer for [WhitelistConfig].
 *
 * @see WhitelistConfig
 * @see WhitelistConfigSurrogate
 */
object WhitelistConfigSerializer : SurrogateBasedSerializer<WhitelistConfig, WhitelistConfigSurrogate>(
    WhitelistConfigSurrogate.serializer(),
    WhitelistConfigSurrogate
)
