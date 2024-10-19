package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.MentionResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.parsing.TokenProcessor
import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.TokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes a user mention
 *
 * @constructor Create a user mention token processor
 */
class UserMentionTokenProcessor : TokenProcessor<TokenProcessingResult, Unit> {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        private const val USER_MENTION_START_INDEX = 1
    }

    override fun tokenType(): TokenType = ChatComponent.USER_MENTION

    @Language("RegExp")
    override fun regexPattern(): String = "@[^\\s@]+"

    override suspend fun process(
        token: Token,
        additionalData: Unit,
    ): TokenProcessingResult {
        val nameString = token.string.substring(USER_MENTION_START_INDEX)

        return MentionResult.userMention(nameString)
    }
}
