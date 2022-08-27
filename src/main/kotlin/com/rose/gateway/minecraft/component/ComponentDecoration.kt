package com.rose.gateway.minecraft.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration

/**
 * Adds a [TextDecoration] to a [Component] to make it italicized.
 *
 * @return The italicized [Component].
 */
fun Component.italic(): Component = this.decorate(TextDecoration.ITALIC)

/**
 * Adds a [TextDecoration] to a [Component] to make it underlined.
 *
 * @return The underlined [Component].
 */
fun Component.underlined(): Component = this.decorate(TextDecoration.UNDERLINED)
