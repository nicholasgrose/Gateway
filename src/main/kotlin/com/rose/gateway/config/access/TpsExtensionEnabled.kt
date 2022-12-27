package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig

/**
 * Gives whether the TPS extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.tpsExtensionEnabled(): Boolean {
    return config.bot.extensions.tps.enabled
}
