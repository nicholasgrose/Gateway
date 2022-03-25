package com.rose.gateway.minecraft.chat.processing

import net.kyori.adventure.text.Component

data class MessageProcessingResult(
    val successful: Boolean,
    val minecraftMessage: Component,
    val discordMessage: String
)
