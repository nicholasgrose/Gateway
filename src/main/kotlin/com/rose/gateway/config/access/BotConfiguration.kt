package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig

/**
 * Gives the Discord bot token from the plugin config
 *
 * @return The Discord bot's token
 */
fun PluginConfig.botToken(): String {
    return config.bot.token
}

/**
 * Gives the bot channels from the plugin config
 *
 * @return The Discord bot's bot channels
 */
fun PluginConfig.botChannels(): List<String> {
    return config.bot.botChannels
}
