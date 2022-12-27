package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import kotlinx.serialization.Serializable

/**
 * Config options for Minecraft
 *
 * @property primaryColor Used to indicate success, color headers, and mark Discord mentions in-game
 * @property secondaryColor Used to indicate information, differentiate text, and mark names of Discord users in-game
 * @property tertiaryColor Used to indicate configuration paths and mark less important info
 * @property warningColor Used to indicate errors/warnings and mark configurations that can be null
 * @constructor Create empty Minecraft config
 */
@Serializable
data class MinecraftConfig(
    @ConfigItem("Used to indicate success, color headers, and mark Discord mentions in-game")
    var primaryColor: String,
    @ConfigItem("Used to indicate information, differentiate text, and mark names of Discord users in-game")
    var secondaryColor: String,
    @ConfigItem("Used to indicate configuration paths and mark less important info")
    var tertiaryColor: String,
    @ConfigItem("Used to indicate errors/warnings and mark configurations that can be null")
    var warningColor: String
) : ConfigObject
