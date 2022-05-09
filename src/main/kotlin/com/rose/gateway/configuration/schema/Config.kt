package com.rose.gateway.configuration.schema

import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.ConfigObject

data class Config(
    @ConfigItem
    var bot: BotConfig,
    @ConfigItem
    var minecraft: MinecraftConfig
) : ConfigObject
