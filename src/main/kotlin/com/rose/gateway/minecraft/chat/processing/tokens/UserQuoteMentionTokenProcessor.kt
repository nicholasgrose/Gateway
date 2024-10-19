package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.MentionResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.parsing.TokenProcessor
import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.TokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes a user quote-mention
 *
 * @constructor Create a role quote-mention token processor
 */
class UserQuoteMentionTokenProcessor : TokenProcessor<TokenProcessingResult, Unit> {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        private const val USER_QUOTE_MENTION_START_INDEX = 2
    }

    override fun tokenType(): TokenType = ChatComponent.USER_QUOTE_MENTION

    @Language("RegExp")
    override fun regexPattern(): String = "@\"((\\\\\")|[^\"])+\""

    override suspend fun process(
        token: Token,
        additionalData: Unit,
    ): TokenProcessingResult {
        val tokenString = token.string
        val nameString = tokenString.substring(USER_QUOTE_MENTION_START_INDEX until tokenString.length - 1)

        return MentionResult.userMention(nameString)
    }
}
