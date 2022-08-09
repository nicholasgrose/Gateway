package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.config.markers.SurrogateBasedSerializer
import com.rose.gateway.config.markers.SurrogateConverter
import com.rose.gateway.discord.bot.extensions.about.AboutExtension
import kotlinx.serialization.Serializable

@Serializable(with = AboutConfigSerializer::class)
class AboutConfig(
    enabled: Boolean
) : CommonExtensionConfig<AboutConfig>(enabled, AboutExtension.extensionName())

@Serializable
data class AboutConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<AboutConfig, AboutConfigSurrogate> {
        override fun fromBase(base: AboutConfig): AboutConfigSurrogate = AboutConfigSurrogate(base.enabled)

        override fun toBase(surrogate: AboutConfigSurrogate): AboutConfig = AboutConfig(surrogate.enabled)
    }
}

object AboutConfigSerializer :
    SurrogateBasedSerializer<AboutConfig, AboutConfigSurrogate>(AboutConfigSurrogate.serializer(), AboutConfigSurrogate)
