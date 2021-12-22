package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.configuration.specs.PluginSpec

object BotConfiguration {
    fun PluginConfiguration.botToken(): String {
        return get(PluginSpec.botToken) ?: ""
    }

    fun PluginConfiguration.memberQueryMax(): Int {
        return get(PluginSpec.BotSpec.memberQueryMax) ?: 0
    }

    fun PluginConfiguration.botChannels(): List<String> {
        return get(PluginSpec.BotSpec.botChannels) ?: listOf()
    }

    fun PluginConfiguration.aboutExtensionEnabled(): Boolean {
        return get(PluginSpec.BotSpec.ExtensionsSpec.AboutSpec.enabled) ?: false
    }

    fun PluginConfiguration.chatExtensionEnabled(): Boolean {
        return get(PluginSpec.BotSpec.ExtensionsSpec.ChatSpec.enabled) ?: false
    }

    fun PluginConfiguration.ipExtensionEnabled(): Boolean {
        return get(PluginSpec.BotSpec.ExtensionsSpec.IpSpec.enabled) ?: false
    }

    fun PluginConfiguration.displayIp(): String {
        return get(PluginSpec.BotSpec.ExtensionsSpec.IpSpec.displayIp) ?: ""
    }

    fun PluginConfiguration.listExtensionEnabled(): Boolean {
        return get(PluginSpec.BotSpec.ExtensionsSpec.ListSpec.enabled) ?: false
    }

    fun PluginConfiguration.whitelistExtensionEnabled(): Boolean {
        return get(PluginSpec.BotSpec.ExtensionsSpec.WhitelistSpec.enabled) ?: false
    }
}
