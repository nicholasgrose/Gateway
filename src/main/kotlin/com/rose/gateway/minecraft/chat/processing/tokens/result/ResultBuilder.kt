package com.rose.gateway.minecraft.chat.processing.tokens.result

import com.rose.gateway.shared.component.ComponentBuilder

class ResultBuilder {
    fun mentionResult(minecraftText: String, discordText: String): TokenProcessingResult {
        return TokenProcessingResult(
            ComponentBuilder.primaryComponent(minecraftText),
            discordText
        )
    }

    fun errorResult(text: String): TokenProcessingResult {
        return TokenProcessingResult(
            ComponentBuilder.errorComponent(text),
            text
        )
    }
}
