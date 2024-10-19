package com.rose.gateway.minecraft.component

import net.kyori.adventure.text.Component

/**
 * Creates a [Component] for a config item
 *
 * @param itemName The name of the item to create the [Component] for
 * @return The config item [Component]
 */
fun item(itemName: String): Component =
    itemName
        .tertiaryComponent()
        .italic()
        .showTextOnHover(
            "Get help for ".component() + itemName.tertiaryComponent().italic(),
        ).runCommandOnClick("/gateway config help $itemName")
