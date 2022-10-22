package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import kotlinx.serialization.Serializable

/**
 * Config options for Minecraft
 *
 * @property primaryColor The color used for labels and Discord mentions in-game
 * @property secondaryColor The color used for differentiated text elements and names of Discord users in-game
 * @property tertiaryColor The color used for labelling config paths
 * @property warningColor The color used for marking problems and config items that can be null
 * @constructor Create empty Minecraft config
 */
@Serializable
data class MinecraftConfig(
    @ConfigItem("Used for labels and Discord mentions in-game.")
    var primaryColor: String,
    @ConfigItem("Used for differentiated text elements and names of Discord users in-game.")
    var secondaryColor: String,
    @ConfigItem("Used for labelling config paths.")
    var tertiaryColor: String,
    @ConfigItem("Used for marking problems and config items that can be null.")
    var warningColor: String
) : ConfigObject
