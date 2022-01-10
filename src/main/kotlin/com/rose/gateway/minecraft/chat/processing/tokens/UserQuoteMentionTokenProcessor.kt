package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.UserMentionBuilder
import guru.zoroark.lixy.LixyToken

class UserQuoteMentionTokenProcessor(plugin: GatewayPlugin) : ChatTokenProcessor {
    companion object {
        const val USER_QUOTE_MENTION_START_INDEX = 2
    }

    private val mentionBuilder = UserMentionBuilder(plugin)

    override fun regexPattern(): String {
        return "@\"((\\\\\")|[^\"])+\""
    }

    override suspend fun processToken(token: LixyToken): TokenProcessingResult {
        val tokenString = token.string
        val nameString = tokenString.substring(USER_QUOTE_MENTION_START_INDEX until tokenString.length)

        return mentionBuilder.createUserMention(nameString)
    }
}
