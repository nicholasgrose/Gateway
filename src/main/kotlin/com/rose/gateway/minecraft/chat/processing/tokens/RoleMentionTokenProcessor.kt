package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.MentionResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.parsing.TokenProcessor
import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.TokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes a role mention
 *
 * @constructor Create a role mention token processor
 */
class RoleMentionTokenProcessor : TokenProcessor<TokenProcessingResult, Unit> {
    companion object {
        private const val ROLE_MENTION_START_INDEX = 3
    }

    override fun tokenType(): TokenType {
        return ChatComponent.ROLE_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "@[rR]=[^\\s@]+"
    }

    override suspend fun process(
        token: Token,
        additionalData: Unit,
    ): TokenProcessingResult {
        val roleString = token.string.substring(ROLE_MENTION_START_INDEX)

        return MentionResult.role(roleString)
    }
}
