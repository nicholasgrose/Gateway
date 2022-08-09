package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    @ConfigItem
    val bot: BotConfig,
    @ConfigItem
    val minecraft: MinecraftConfig
) : ConfigObject
