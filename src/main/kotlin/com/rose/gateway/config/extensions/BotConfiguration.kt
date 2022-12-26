package com.rose.gateway.config.extensions

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

/**
 * Gives whether the about extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.aboutExtensionEnabled(): Boolean {
    return config.bot.extensions.about.enabled
}

/**
 * Gives whether the chat extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.chatExtensionEnabled(): Boolean {
    return config.bot.extensions.chat.enabled
}

/**
 * Gives whether the ip extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.ipExtensionEnabled(): Boolean {
    return config.bot.extensions.ip.enabled
}

/**
 * The IP to display with the IP extension
 *
 * @return The IP to display
 */
fun PluginConfig.displayIp(): String {
    return config.bot.extensions.ip.displayIp
}

/**
 * Gives whether the list extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.listExtensionEnabled(): Boolean {
    return config.bot.extensions.list.enabled
}

/**
 * Gives whether the TPS extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.tpsExtensionEnabled(): Boolean {
    return config.bot.extensions.tps.enabled
}

/**
 * Gives whether the whitelist extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.whitelistExtensionEnabled(): Boolean {
    return config.bot.extensions.whitelist.enabled
}
