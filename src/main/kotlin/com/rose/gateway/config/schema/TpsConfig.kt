package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.discord.bot.extensions.tps.TpsExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable

/**
 * Config options for the "tps extension"
 *
 * @constructor Creates a "tps config" with the provided data
 *
 * @param enabled Whether the extension is enabled
 */
@Serializable(with = TpsConfigSerializer::class)
class TpsConfig(
    enabled: Boolean,
) : CommonExtensionConfig(enabled, TpsExtension.extensionName())

/**
 * Surrogate for serialization of [TpsConfig]
 *
 * @property enabled Whether the extension is enabled
 * @constructor Create a "tps config" surrogate with the provided data
 *
 * @see TpsConfig
 * @see TpsConfigSerializer
 */
@Serializable
data class TpsConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<TpsConfig, TpsConfigSurrogate> {
        override fun fromBase(base: TpsConfig): TpsConfigSurrogate = TpsConfigSurrogate(base.enabled)

        override fun toBase(surrogate: TpsConfigSurrogate): TpsConfig = TpsConfig(surrogate.enabled)
    }
}

/**
 * Serializer for [TpsConfig]
 *
 * @see TpsConfig
 * @see TpsConfigSurrogate
 */
object TpsConfigSerializer : SurrogateBasedSerializer<TpsConfig, TpsConfigSurrogate>(
    TpsConfigSurrogate.serializer(),
    TpsConfigSurrogate,
)
