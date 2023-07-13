package com.rose.gateway.minecraft.chat.processing

import net.kyori.adventure.text.Component

/**
 * The result of processing a single Minecraft message
 *
 * @property successful Whether processing succeeded
 * @property minecraftMessage The processed message prepared for Minecraft
 * @property discordMessage The processed message prepared for Discord
 * @constructor Create a message processing result
 */
data class MessageProcessingResult(
    val successful: Boolean,
    val minecraftMessage: Component,
    val discordMessage: String,
)
