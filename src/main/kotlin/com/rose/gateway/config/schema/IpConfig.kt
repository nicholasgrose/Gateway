package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.discord.bot.extensions.ip.IpExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable

/**
 * Config options for the "IP extension"
 *
 * @property displayIp The IP that will be displayed by the IP command
 * @constructor Creates an "IP config" with the provided data
 *
 * @param enabled Whether the extension is enabled
 */
@Serializable(with = IpConfigSerializer::class)
class IpConfig(
    enabled: Boolean,
    @ConfigItem("The IP that will be displayed by the IP command.")
    var displayIp: String,
) : CommonExtensionConfig(enabled, IpExtension.extensionName())

/**
 * Surrogate for serialization of [IpConfig]
 *
 * @property enabled Whether the extension is enabled
 * @property displayIp The IP to display to users requesting the IP
 * @constructor Create an "IP config" surrogate with the provided data
 *
 * @see IpConfig
 * @see IpConfigSerializer
 */
@Serializable
data class IpConfigSurrogate(
    val enabled: Boolean,
    val displayIp: String,
) {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object : SurrogateConverter<IpConfig, IpConfigSurrogate> {
        override fun fromBase(base: IpConfig): IpConfigSurrogate = IpConfigSurrogate(base.enabled, base.displayIp)

        override fun toBase(surrogate: IpConfigSurrogate): IpConfig = IpConfig(surrogate.enabled, surrogate.displayIp)
    }
}

/**
 * Serializer for [IpConfig]
 *
 * @see IpConfig
 * @see IpConfigSurrogate
 */
object IpConfigSerializer :
    SurrogateBasedSerializer<IpConfig, IpConfigSurrogate>(IpConfigSurrogate.serializer(), IpConfigSurrogate)
