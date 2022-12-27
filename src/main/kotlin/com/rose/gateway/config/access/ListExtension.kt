package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig

/**
 * Gives whether the list extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.listExtensionEnabled(): Boolean {
    return config.bot.extensions.list.enabled
}

/**
 * Gives the maximum number of players that can be displayed on each list page
 *
 * @return The maximum number of players per page
 */
fun PluginConfig.maxPlayersPerListPage(): Int {
    return config.bot.extensions.list.maxPlayersPerPage
}
