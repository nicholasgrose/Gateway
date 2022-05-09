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
        return config?.bot?.extension?.about?.enabled ?: false
    }

    fun PluginConfiguration.chatExtensionEnabled(): Boolean {
        return config?.bot?.extension?.chat?.enabled ?: false
    }

    fun PluginConfiguration.ipExtensionEnabled(): Boolean {
        return config?.bot?.extension?.ip?.enabled ?: false
    }

    fun PluginConfiguration.displayIp(): String {
        return config?.bot?.extension?.ip?.displayIp ?: ""
    }

    fun PluginConfiguration.listExtensionEnabled(): Boolean {
        return config?.bot?.extension?.list?.enabled ?: false
    }

    fun PluginConfiguration.whitelistExtensionEnabled(): Boolean {
        return config?.bot?.extension?.whitelist?.enabled ?: false
    }
}
