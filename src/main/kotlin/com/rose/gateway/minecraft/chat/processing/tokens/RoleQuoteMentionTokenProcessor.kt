package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.RoleMentionBuilder
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.ChatTokenProcessor
import guru.zoroark.lixy.LixyToken

class RoleQuoteMentionTokenProcessor(plugin: GatewayPlugin) : ChatTokenProcessor<TokenProcessingResult> {
    companion object {
        const val ROLE_QUOTE_MENTION_START_INDEX = 4
    }

    private val mentionBuilder = RoleMentionBuilder(plugin)

    override fun regexPattern(): String {
        return "@R=\"((\\\\\")|[^\"])+\""
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val tokenString = token.string
        val roleString = tokenString.substring(ROLE_QUOTE_MENTION_START_INDEX until tokenString.length)

        return mentionBuilder.createRoleMention(roleString)
    }
}
