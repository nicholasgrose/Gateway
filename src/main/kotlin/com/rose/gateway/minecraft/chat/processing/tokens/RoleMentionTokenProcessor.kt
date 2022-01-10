package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.RoleMentionBuilder
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.ChatTokenProcessor
import guru.zoroark.lixy.LixyToken

class RoleMentionTokenProcessor(plugin: GatewayPlugin) : ChatTokenProcessor<TokenProcessingResult> {
    companion object {
        const val ROLE_MENTION_START_INDEX = 3
    }

    private val mentionBuilder = RoleMentionBuilder(plugin)

    override fun regexPattern(): String {
        return "@R=[^\\s@]+"
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val roleString = token.string.substring(ROLE_MENTION_START_INDEX)

        return mentionBuilder.createRoleMention(roleString)
    }
}
