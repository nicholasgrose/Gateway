package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.configuration.specs.PluginSpec

object BotConfiguration {

    fun PluginConfiguration.commandTimeout(): Long {
        return configuration!![PluginSpec.BotSpec.commandTimeout].toLong()
    }

    fun PluginConfiguration.memberQueryMax(): Int {
        return configuration!![PluginSpec.BotSpec.memberQueryMax]
    }

    fun PluginConfiguration.botChannels(): List<String> {
        return configuration!![PluginSpec.BotSpec.botChannels]
    }
}
