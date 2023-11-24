package com.rose.gateway.minecraft.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent

/**
 * Adds a click event to the [Component] that runs on a command when clicked
 *
 * @param command The command to run on click
 * @return The [Component] with the click event added
 */
fun Component.runCommandOnClick(command: String): Component =
    this.clickEvent(
        ClickEvent.runCommand(command),
    )

/**
 * Adds a click event to the [Component] that opens a URL when clicked
 *
 * @param url The URL to open on click
 * @return The [Component] with the click event added
 */
fun Component.openUrlOnClick(url: String): Component =
    this.clickEvent(
        ClickEvent.openUrl(url),
    )
