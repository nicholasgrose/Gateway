package com.rose.gateway.configuration.schema

import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.ConfigObject
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    @ConfigItem
    val bot: BotConfig,
    @ConfigItem
    val minecraft: MinecraftConfig
) : ConfigObject
