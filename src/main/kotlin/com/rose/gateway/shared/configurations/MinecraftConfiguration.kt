package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.shared.color.ColorParser.asTextColor
import net.kyori.adventure.text.format.TextColor

object MinecraftConfiguration {
    private const val DEFAULT_PRIMARY_COLOR = "#56EE5C"
    private const val DEFAULT_SECONDARY_COLOR = "#7289DA"
    private const val DEFAULT_TERTIARY_COLOR = "#F526ED"
    private const val DEFAULT_WARNING_COLOR = "#EB4325"

    fun PluginConfiguration.primaryColor(): TextColor {
        return config?.minecraft?.primaryColor?.asTextColor() ?: DEFAULT_PRIMARY_COLOR.asTextColor()
    }

    fun PluginConfiguration.secondaryColor(): TextColor {
        return config?.minecraft?.secondaryColor?.asTextColor() ?: DEFAULT_SECONDARY_COLOR.asTextColor()
    }

    fun PluginConfiguration.tertiaryColor(): TextColor {
        return config?.minecraft?.tertiaryColor?.asTextColor() ?: DEFAULT_TERTIARY_COLOR.asTextColor()
    }

    fun PluginConfiguration.warningColor(): TextColor {
        return config?.minecraft?.warningColor?.asTextColor() ?: DEFAULT_WARNING_COLOR.asTextColor()
    }
}
