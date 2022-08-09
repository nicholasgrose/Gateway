package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
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
