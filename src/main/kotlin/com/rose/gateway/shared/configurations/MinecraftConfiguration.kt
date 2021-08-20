package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.configuration.specs.PluginSpec
import com.rose.gateway.shared.color.ColorParser.asTextColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.uchuhimo.konf.Item
import net.kyori.adventure.text.format.TextColor

object MinecraftConfiguration {
    fun PluginConfiguration.primaryColor(): TextColor {
        return if (notLoaded()) PluginSpec.MinecraftSpec.DEFAULT_PRIMARY_COLOR.asTextColor()
        else configurationAsTextColor(PluginSpec.MinecraftSpec.primaryColor)
    }

    private fun PluginConfiguration.configurationAsTextColor(spec: Item<String>): TextColor {
        return get(spec).asTextColor()
    }

    fun PluginConfiguration.secondaryColor(): TextColor {
        return if (notLoaded()) PluginSpec.MinecraftSpec.DEFAULT_SECONDARY_COLOR.asTextColor()
        else configurationAsTextColor(PluginSpec.MinecraftSpec.secondaryColor)
    }

    fun PluginConfiguration.tertiaryColor(): TextColor {
        return if (notLoaded()) PluginSpec.MinecraftSpec.DEFAULT_TERTIARY_COLOR.asTextColor()
        else configurationAsTextColor(PluginSpec.MinecraftSpec.tertiaryColor)
    }

    fun PluginConfiguration.warningColor(): TextColor {
        return if (notLoaded()) PluginSpec.MinecraftSpec.DEFAULT_WARNING_COLOR.asTextColor()
        else configurationAsTextColor(PluginSpec.MinecraftSpec.warningColor)
    }
}
