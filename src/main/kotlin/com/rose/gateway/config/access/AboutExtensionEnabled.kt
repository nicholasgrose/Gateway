package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig

/**
 * Gives whether the about extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.aboutExtensionEnabled(): Boolean = config.bot.extensions.about.enabled
