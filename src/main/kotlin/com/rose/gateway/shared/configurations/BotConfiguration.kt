package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration

object BotConfiguration {
    fun PluginConfiguration.botToken(): String {
        return config?.bot?.token ?: ""
    }

    fun PluginConfiguration.botChannels(): List<String> {
        return config?.bot?.botChannels ?: listOf()
    }

    fun PluginConfiguration.aboutExtensionEnabled(): Boolean {
        return config?.bot?.extensions?.about?.enabled ?: false
    }

    fun PluginConfiguration.chatExtensionEnabled(): Boolean {
        return config?.bot?.extensions?.chat?.enabled ?: false
    }

    fun PluginConfiguration.ipExtensionEnabled(): Boolean {
        return config?.bot?.extensions?.ip?.enabled ?: false
    }

    fun PluginConfiguration.displayIp(): String {
        return config?.bot?.extensions?.ip?.displayIp ?: ""
    }

    fun PluginConfiguration.listExtensionEnabled(): Boolean {
        return config?.bot?.extensions?.list?.enabled ?: false
    }

    fun PluginConfiguration.whitelistExtensionEnabled(): Boolean {
        return config?.bot?.extensions?.whitelist?.enabled ?: false
    }
}
