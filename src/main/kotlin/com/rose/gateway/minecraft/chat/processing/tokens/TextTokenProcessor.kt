package com.rose.gateway.minecraft.chat.processing.tokens

import guru.zoroark.lixy.LixyToken
import net.kyori.adventure.text.Component

class TextTokenProcessor : ChatTokenProcessor {
    override fun regexPattern(): String {
        return ".[^@]*"
    }

    override suspend fun processToken(token: LixyToken): TokenProcessingResult {
        val text = token.string

        return TokenProcessingResult(Component.text(text), text)
    }
}
