package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.UserMentionBuilder
import com.rose.gateway.shared.processing.TokenProcessor
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import org.intellij.lang.annotations.Language

class UserMentionTokenProcessor(plugin: GatewayPlugin) : TokenProcessor<TokenProcessingResult, Unit> {
    companion object {
        const val USER_MENTION_START_INDEX = 1
    }

    private val mentionBuilder = UserMentionBuilder(plugin)

    override fun tokenType(): LixyTokenType {
        return ChatComponent.USER_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "@[^\\s@]+"
    }

    override suspend fun process(token: LixyToken, additionalData: Unit): TokenProcessingResult {
        val nameString = token.string.substring(USER_MENTION_START_INDEX)

        return mentionBuilder.createUserMention(nameString)
    }
}
