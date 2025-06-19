package xyz.rose.gateway.core.core.config.schema

import kotlinx.serialization.Serializable
import xyz.rose.gateway.config.schema.MinecraftConfig
import xyz.rose.gateway.core.core.config.markers.ConfigItem
import xyz.rose.gateway.core.core.config.markers.ConfigObject

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
    @ConfigItem val minecraft: MinecraftConfig,
) : ConfigObject
