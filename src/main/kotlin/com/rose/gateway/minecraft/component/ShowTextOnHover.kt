package com.rose.gateway.minecraft.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent

/**
 * Adds a piece of text to show on mouse hover.
 *
 * @param component The [Component] to show.
 * @return The [Component] with the hover event.
 */
fun Component.showTextOnHover(component: Component): Component = this.hoverEvent(
    HoverEvent.showText(component)
)
