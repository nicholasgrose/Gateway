package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.shared.parsing.TokenProcessor
import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.TokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes a piece of plaintext
 *
 * @constructor Create a text token processor
 */
class TextTokenProcessor : TokenProcessor<TokenProcessingResult, Unit> {
    override fun tokenType(): TokenType = ChatComponent.TEXT

    @Language("RegExp")
    override fun regexPattern(): String = ".[^@]*"

    override suspend fun process(
        token: Token,
        additionalData: Unit,
    ): TokenProcessingResult {
        val text = token.string

        return TokenProcessingResult(text.component(), text)
    }
}
