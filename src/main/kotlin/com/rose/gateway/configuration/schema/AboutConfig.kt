package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.SurrogateBasedSerializer
import com.rose.gateway.configuration.markers.SurrogateConverter
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
