package com.rose.gateway.configuration.schema

import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.ConfigObject

data class ExtensionConfig(
    @ConfigItem
    var about: AboutConfig,
    @ConfigItem
    var chat: ChatConfig,
    @ConfigItem
    var ip: IpConfig,
    @ConfigItem
    var list: ListConfig,
    @ConfigItem
    var whitelist: WhitelistConfig
) : ConfigObject
