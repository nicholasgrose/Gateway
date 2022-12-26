package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.MentionResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.parsing.TokenProcessor
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes a role quote-mention
 *
 * @constructor Create a role quote-mention token processor
 */
class RoleQuoteMentionTokenProcessor : TokenProcessor<TokenProcessingResult, Unit> {
    companion object {
        private const val ROLE_QUOTE_MENTION_START_INDEX = 4
    }

    override fun tokenType(): LixyTokenType {
        return ChatComponent.ROLE_QUOTE_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "@R=\"((\\\\\")|[^\"])+\""
    }

    override suspend fun process(token: LixyToken, additionalData: Unit): TokenProcessingResult {
        val tokenString = token.string
        val roleString = tokenString.substring(ROLE_QUOTE_MENTION_START_INDEX until tokenString.length)

        return MentionResult.role(roleString)
    }
}
