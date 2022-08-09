package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.CommonExtensionConfig
import com.rose.gateway.config.markers.SurrogateBasedSerializer
import com.rose.gateway.config.markers.SurrogateConverter
import com.rose.gateway.discord.bot.extensions.list.ListExtension
import kotlinx.serialization.Serializable

@Serializable(with = ListConfigSerializer::class)
class ListConfig(
    enabled: Boolean
) : CommonExtensionConfig<ListConfig>(enabled, ListExtension.extensionName())

@Serializable
data class ListConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<ListConfig, ListConfigSurrogate> {
        override fun fromBase(base: ListConfig): ListConfigSurrogate = ListConfigSurrogate(base.enabled)

        override fun toBase(surrogate: ListConfigSurrogate): ListConfig = ListConfig(surrogate.enabled)
    }
}

object ListConfigSerializer :
    SurrogateBasedSerializer<ListConfig, ListConfigSurrogate>(ListConfigSurrogate.serializer(), ListConfigSurrogate)
