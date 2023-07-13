package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.discord.bot.extensions.about.AboutExtension
import com.rose.gateway.shared.serialization.SurrogateBasedSerializer
import com.rose.gateway.shared.serialization.SurrogateConverter
import kotlinx.serialization.Serializable

/**
 * Config options for the "about extension"
 *
 * @constructor Creates an "about config" with the provided data
 *
 * @param enabled Whether the extension is enabled
 */
@Serializable(with = AboutConfigSerializer::class)
class AboutConfig(
    enabled: Boolean,
) : CommonExtensionConfig(enabled, AboutExtension.extensionName())

/**
 * Surrogate for serialization of [AboutConfig]
 *
 * @property enabled Whether the extension is enabled
 * @constructor Create an "about config" surrogate with the provided data
 *
 * @see AboutConfig
 * @see AboutConfigSerializer
 */
@Serializable
data class AboutConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<AboutConfig, AboutConfigSurrogate> {
        override fun fromBase(base: AboutConfig): AboutConfigSurrogate = AboutConfigSurrogate(base.enabled)

        override fun toBase(surrogate: AboutConfigSurrogate): AboutConfig = AboutConfig(surrogate.enabled)
    }
}

/**
 * Serializer for [AboutConfig]
 *
 * @see AboutConfig
 * @see AboutConfigSurrogate
 */
object AboutConfigSerializer :
    SurrogateBasedSerializer<AboutConfig, AboutConfigSurrogate>(AboutConfigSurrogate.serializer(), AboutConfigSurrogate)
