package com.rose.gateway.config.extensions

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.color.asTextColor
import net.kyori.adventure.text.format.TextColor

fun PluginConfig.primaryColor(): TextColor {
    return config.minecraft.primaryColor.asTextColor()
}

fun PluginConfig.secondaryColor(): TextColor {
    return config.minecraft.secondaryColor.asTextColor()
}

fun PluginConfig.tertiaryColor(): TextColor {
    return config.minecraft.tertiaryColor.asTextColor()
}

fun PluginConfig.warningColor(): TextColor {
    return config.minecraft.warningColor.asTextColor()
}
