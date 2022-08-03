package com.rose.gateway.configuration.schema

import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.ConfigObject
import kotlinx.serialization.Serializable

@Serializable
data class ExtensionsConfig(
    @ConfigItem
    val about: AboutConfig,
    @ConfigItem
    val chat: ChatConfig,
    @ConfigItem
    val ip: IpConfig,
    @ConfigItem
    val list: ListConfig,
    @ConfigItem
    val whitelist: WhitelistConfig
) : ConfigObject
