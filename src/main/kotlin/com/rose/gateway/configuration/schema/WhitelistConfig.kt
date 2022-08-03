package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.SurrogateBasedSerializer
import com.rose.gateway.configuration.markers.SurrogateConverter
import kotlinx.serialization.Serializable

@Serializable(with = WhitelistConfigSerializer::class)
class WhitelistConfig(
    enabled: Boolean
) : CommonExtensionConfig<WhitelistConfig>(enabled, WhitelistExtension.extensionName())

@Serializable
data class WhitelistConfigSurrogate(val enabled: Boolean) {
    companion object : SurrogateConverter<WhitelistConfig, WhitelistConfigSurrogate> {
        override fun fromBase(base: WhitelistConfig): WhitelistConfigSurrogate = WhitelistConfigSurrogate(base.enabled)

        override fun toBase(surrogate: WhitelistConfigSurrogate): WhitelistConfig = WhitelistConfig(surrogate.enabled)
    }
}

object WhitelistConfigSerializer :
    SurrogateBasedSerializer<WhitelistConfig, WhitelistConfigSurrogate>(
        WhitelistConfigSurrogate.serializer(),
        WhitelistConfigSurrogate
    )
