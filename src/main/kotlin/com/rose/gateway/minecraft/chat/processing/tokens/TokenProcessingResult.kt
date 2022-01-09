package com.rose.gateway.minecraft.chat.processing.tokens

import net.kyori.adventure.text.Component

data class TokenProcessingResult(
    val minecraftMessage: Component,
    val discordMessage: String
)
