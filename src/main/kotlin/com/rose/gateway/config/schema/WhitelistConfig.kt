package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.discord.bot.extensions.whitelist.WhitelistExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable

/**
 * Config options for the "whitelist extension"
 *
 * @property maxPlayersPerPage The maximum number of player allowed per list page
 * @constructor Creates a "whitelist config" with the provided data
 *
 * @param enabled Whether the extension is enabled
 */
@Serializable(with = WhitelistConfigSerializer::class)
class WhitelistConfig(
    enabled: Boolean,
    /**
     * The maximum number of player allowed per list page
     */
    @ConfigItem("The maximum number of player allowed per list page") var maxPlayersPerPage: Int,
) : CommonExtensionConfig(enabled, WhitelistExtension.extensionName())

/**
 * Surrogate for serialization of [WhitelistConfig]
 *
 * @property enabled Whether the extension is enabled
 * @property maxPlayersPerPage The maximum number of player allowed per list page
 * @constructor Create a "whitelist config" surrogate with the provided data
 *
 * @see WhitelistConfig
 * @see WhitelistConfigSerializer
 */
@Serializable
data class WhitelistConfigSurrogate(val enabled: Boolean, val maxPlayersPerPage: Int) {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object : SurrogateConverter<WhitelistConfig, WhitelistConfigSurrogate> {
        override fun fromBase(base: WhitelistConfig): WhitelistConfigSurrogate =
            WhitelistConfigSurrogate(base.enabled, base.maxPlayersPerPage)

        override fun toBase(surrogate: WhitelistConfigSurrogate): WhitelistConfig =
            WhitelistConfig(surrogate.enabled, surrogate.maxPlayersPerPage)
    }
}

/**
 * Serializer for [WhitelistConfig]
 *
 * @see WhitelistConfig
 * @see WhitelistConfigSurrogate
 */
object WhitelistConfigSerializer : SurrogateBasedSerializer<WhitelistConfig, WhitelistConfigSurrogate>(
    WhitelistConfigSurrogate.serializer(),
    WhitelistConfigSurrogate,
)
