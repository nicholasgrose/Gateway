package com.rose.gateway.minecraft.color

import net.kyori.adventure.text.format.TextColor

/**
 * Converts the [String] to a Minecraft [TextColor] as if it were in hexadecimal.
 *
 * @return The string as a text color.
 */
fun String.asTextColor(): TextColor {
    return TextColor.fromHexString(this)!!
}
