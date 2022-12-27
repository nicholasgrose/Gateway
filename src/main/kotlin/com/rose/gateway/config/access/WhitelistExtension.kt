package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig

/**
 * Gives whether the whitelist extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.whitelistExtensionEnabled(): Boolean {
    return config.bot.extensions.whitelist.enabled
}

/**
 * Gives the maximum number of players that can be displayed on each whitelist page
 *
 * @return The maximum number of players per page
 */
fun PluginConfig.maxPlayersPerWhitelistPage(): Int {
    return config.bot.extensions.whitelist.maxPlayersPerPage
}
