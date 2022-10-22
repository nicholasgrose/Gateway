package com.rose.gateway.minecraft.component

import net.kyori.adventure.text.Component

/**
 * Creates a [Component] for a config item
 *
 * @param itemName The name of the item to create the [Component] for
 * @return The config item [Component]
 */
fun item(itemName: String): Component {
    return ColorComponent.tertiary(itemName).italic().showTextOnHover(
        Component.text("Get help for ") + ColorComponent.tertiary(itemName).italic()
    ).runCommandOnClick("/gateway config help $itemName")
}
