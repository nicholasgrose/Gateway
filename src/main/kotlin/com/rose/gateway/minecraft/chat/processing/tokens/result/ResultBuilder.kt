package com.rose.gateway.minecraft.chat.processing.tokens.result

import com.rose.gateway.minecraft.component.ColorComponent

class ResultBuilder {
    fun mentionResult(minecraftText: String, discordText: String): TokenProcessingResult {
        return TokenProcessingResult(
            ColorComponent.primary(minecraftText),
            discordText
        )
    }

    fun errorResult(text: String): TokenProcessingResult {
        return TokenProcessingResult(
            ColorComponent.warning(text),
            text
        )
    }
}
