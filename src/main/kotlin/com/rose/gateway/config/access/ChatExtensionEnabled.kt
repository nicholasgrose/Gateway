package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig

/**
 * Gives whether the chat extension is enabled from the plugin config
 *
 * @return Whether the extension is enabled
 */
fun PluginConfig.chatExtensionEnabled(): Boolean = config.bot.extensions.chat.enabled
