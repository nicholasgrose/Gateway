package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import kotlinx.serialization.Serializable

/**
 * The top level data class for the plugin's config
 *
 * @property bot Config options for the Discord bot
 * @property minecraft Config options for Minecraft
 * @constructor Creates a new config
 */
@Serializable
data class Config(
    @ConfigItem val bot: BotConfig,
    @ConfigItem val minecraft: MinecraftConfig
) : ConfigObject
