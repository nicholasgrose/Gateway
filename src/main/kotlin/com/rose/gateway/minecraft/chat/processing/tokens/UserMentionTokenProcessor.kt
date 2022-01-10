package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.UserMentionBuilder
import com.rose.gateway.shared.processing.ChatTokenProcessor
import guru.zoroark.lixy.LixyToken

class UserMentionTokenProcessor(plugin: GatewayPlugin) : ChatTokenProcessor<TokenProcessingResult> {
    companion object {
        const val USER_MENTION_START_INDEX = 1
    }

    private val mentionBuilder = UserMentionBuilder(plugin)

    override fun regexPattern(): String {
        return "@[^\\s@]+"
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val nameString = token.string.substring(USER_MENTION_START_INDEX)

        return mentionBuilder.createUserMention(nameString)
    }
}
