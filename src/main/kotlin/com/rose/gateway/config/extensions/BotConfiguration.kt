package com.rose.gateway.config.extensions

import com.rose.gateway.config.PluginConfig

fun PluginConfig.botToken(): String {
    return config.bot.token
}

fun PluginConfig.botChannels(): List<String> {
    return config.bot.botChannels
}

fun PluginConfig.aboutExtensionEnabled(): Boolean {
    return config.bot.extensions.about.enabled
}

fun PluginConfig.chatExtensionEnabled(): Boolean {
    return config.bot.extensions.chat.enabled
}

fun PluginConfig.ipExtensionEnabled(): Boolean {
    return config.bot.extensions.ip.enabled
}

fun PluginConfig.displayIp(): String {
    return config.bot.extensions.ip.displayIp
}

fun PluginConfig.listExtensionEnabled(): Boolean {
    return config.bot.extensions.list.enabled
}

fun PluginConfig.whitelistExtensionEnabled(): Boolean {
    return config.bot.extensions.whitelist.enabled
}
