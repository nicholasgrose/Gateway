package com.rose.gateway.minecraft.color

import net.kyori.adventure.text.format.TextColor

fun String.asTextColor(): TextColor {
    return TextColor.fromHexString(this)!!
}
