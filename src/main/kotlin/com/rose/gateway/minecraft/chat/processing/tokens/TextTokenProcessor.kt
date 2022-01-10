package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.ChatTokenProcessor
import guru.zoroark.lixy.LixyToken
import net.kyori.adventure.text.Component

class TextTokenProcessor : ChatTokenProcessor<TokenProcessingResult> {
    override fun regexPattern(): String {
        return ".[^@]*"
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val text = token.string

        return TokenProcessingResult(Component.text(text), text)
    }
}
