package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.configuration.specs.PluginSpec
import com.rose.gateway.shared.color.ColorParser.asTextColor
import com.uchuhimo.konf.Item
import net.kyori.adventure.text.format.TextColor

object MinecraftConfiguration {
    fun PluginConfiguration.primaryColor(): TextColor {
        return configurationAsTextColor(PluginSpec.MinecraftSpec.primaryColor)
            ?: PluginSpec.MinecraftSpec.DEFAULT_PRIMARY_COLOR.asTextColor()
    }

    private fun PluginConfiguration.configurationAsTextColor(spec: Item<String>): TextColor? {
        return get(spec)?.asTextColor()
    }

    fun PluginConfiguration.secondaryColor(): TextColor {
        return configurationAsTextColor(PluginSpec.MinecraftSpec.secondaryColor)
            ?: PluginSpec.MinecraftSpec.DEFAULT_SECONDARY_COLOR.asTextColor()
    }

    fun PluginConfiguration.tertiaryColor(): TextColor {
        return configurationAsTextColor(PluginSpec.MinecraftSpec.tertiaryColor)
            ?: PluginSpec.MinecraftSpec.DEFAULT_TERTIARY_COLOR.asTextColor()
    }

    fun PluginConfiguration.warningColor(): TextColor {
        return configurationAsTextColor(PluginSpec.MinecraftSpec.warningColor)
            ?: PluginSpec.MinecraftSpec.DEFAULT_WARNING_COLOR.asTextColor()
    }
}
