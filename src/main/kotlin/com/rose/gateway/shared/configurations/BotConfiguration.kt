package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration

fun PluginConfiguration.botToken(): String {
    return config.bot.token
}

fun PluginConfiguration.botChannels(): List<String> {
    return config.bot.botChannels
}

fun PluginConfiguration.aboutExtensionEnabled(): Boolean {
    return config.bot.extensions.about.enabled
}

fun PluginConfiguration.chatExtensionEnabled(): Boolean {
    return config.bot.extensions.chat.enabled
}

fun PluginConfiguration.ipExtensionEnabled(): Boolean {
    return config.bot.extensions.ip.enabled
}

fun PluginConfiguration.displayIp(): String {
    return config.bot.extensions.ip.displayIp
}

fun PluginConfiguration.listExtensionEnabled(): Boolean {
    return config.bot.extensions.list.enabled
}

fun PluginConfiguration.whitelistExtensionEnabled(): Boolean {
    return config.bot.extensions.whitelist.enabled
}
