package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig

/**
 * Gives whether the ip extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.ipExtensionEnabled(): Boolean = config.bot.extensions.ip.enabled

/**
 * The IP to display with the IP extension
 *
 * @return The IP to display
 */
fun PluginConfig.displayIp(): String = config.bot.extensions.ip.displayIp
