package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.TokenProcessor
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import org.intellij.lang.annotations.Language

class TextTokenProcessor : TokenProcessor<TokenProcessingResult> {
    override fun tokenType(): LixyTokenType {
        return ChatComponent.TEXT
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return ".[^@]*"
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val text = token.string

        return TokenProcessingResult(Component.text(text), text)
    }
}
