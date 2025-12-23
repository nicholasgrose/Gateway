package xyz.rose.gateway.platform.discord.config

import kotlinx.serialization.Serializable

/**
 * The root config for configuring the [xyz.rose.gateway.platform.discord.DiscordPlatform]
 *
 * @property botToken The token for the Discord bot to authenticate with
 * @constructor Create a new Discord config
 */
@Serializable
data class DiscordConfig(
    val botToken: String,
)
