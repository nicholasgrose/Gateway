package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.UserMentionBuilder
import com.rose.gateway.shared.processing.TokenProcessor
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import org.intellij.lang.annotations.Language

class UserQuoteMentionTokenProcessor(plugin: GatewayPlugin) : TokenProcessor<TokenProcessingResult> {
    companion object {
        const val USER_QUOTE_MENTION_START_INDEX = 2
    }

    private val mentionBuilder = UserMentionBuilder(plugin)

    override fun tokenType(): LixyTokenType {
        return ChatComponent.USER_QUOTE_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "@\"((\\\\\")|[^\"])+\""
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val tokenString = token.string
        val nameString = tokenString.substring(USER_QUOTE_MENTION_START_INDEX until tokenString.length)

        return mentionBuilder.createUserMention(nameString)
    }
}
