package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.ip.IpExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.SurrogateBasedSerializer
import com.rose.gateway.configuration.markers.SurrogateConverter
import kotlinx.serialization.Serializable

@Serializable(with = IpConfigSerializer::class)
class IpConfig(
    enabled: Boolean,
    @ConfigItem("The IP that will be displayed by the IP command.")
    var displayIp: String
) : CommonExtensionConfig<IpConfig>(enabled, IpExtension.extensionName())

@Serializable
data class IpConfigSurrogate(val enabled: Boolean, val displayIp: String) {
    companion object : SurrogateConverter<IpConfig, IpConfigSurrogate> {
        override fun fromBase(base: IpConfig): IpConfigSurrogate = IpConfigSurrogate(base.enabled, base.displayIp)

        override fun toBase(surrogate: IpConfigSurrogate): IpConfig = IpConfig(surrogate.enabled, surrogate.displayIp)
    }
}

object IpConfigSerializer :
    SurrogateBasedSerializer<IpConfig, IpConfigSurrogate>(IpConfigSurrogate.serializer(), IpConfigSurrogate)
