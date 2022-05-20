package com.rose.gateway.shared.configurations

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.shared.color.asTextColor
import net.kyori.adventure.text.format.TextColor

fun PluginConfiguration.primaryColor(): TextColor {
    return config.minecraft.primaryColor.asTextColor()
}

fun PluginConfiguration.secondaryColor(): TextColor {
    return config.minecraft.secondaryColor.asTextColor()
}

fun PluginConfiguration.tertiaryColor(): TextColor {
    return config.minecraft.tertiaryColor.asTextColor()
}

fun PluginConfiguration.warningColor(): TextColor {
    return config.minecraft.warningColor.asTextColor()
}
