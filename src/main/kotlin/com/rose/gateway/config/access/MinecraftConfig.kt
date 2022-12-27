package com.rose.gateway.config.access

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.color.asTextColor
import net.kyori.adventure.text.format.TextColor

/**
 * Gives the Minecraft primary color from the plugin config
 *
 * @return The color in a form usable for Minecraft
 */
fun PluginConfig.primaryColor(): TextColor {
    return config.minecraft.primaryColor.asTextColor()
}

/**
 * Gives the Minecraft secondary color from the plugin config
 *
 * @return The color in a form usable for Minecraft
 */
fun PluginConfig.secondaryColor(): TextColor {
    return config.minecraft.secondaryColor.asTextColor()
}

/**
 * Gives the Minecraft tertiary color from the plugin config
 *
 * @return The color in a form usable for Minecraft
 */
fun PluginConfig.tertiaryColor(): TextColor {
    return config.minecraft.tertiaryColor.asTextColor()
}

/**
 * Gives the Minecraft warning color from the plugin config
 *
 * @return The color in a form usable for Minecraft
 */
fun PluginConfig.warningColor(): TextColor {
    return config.minecraft.warningColor.asTextColor()
}
