package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.SurrogateBasedSerializer
import com.rose.gateway.configuration.markers.SurrogateConverter
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
