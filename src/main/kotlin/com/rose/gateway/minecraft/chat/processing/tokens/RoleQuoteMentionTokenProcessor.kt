package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.shared.RoleMentionBuilder
import guru.zoroark.lixy.LixyToken

class RoleQuoteMentionTokenProcessor(plugin: GatewayPlugin) : ChatTokenProcessor {
    companion object {
        const val ROLE_QUOTE_MENTION_START_INDEX = 4
    }

    private val mentionBuilder = RoleMentionBuilder(plugin)

    override fun regexPattern(): String {
        return "@R=\"((\\\\\")|[^\"])+\""
    }

    override suspend fun processToken(token: LixyToken): TokenProcessingResult {
        val tokenString = token.string
        val roleString = tokenString.substring(ROLE_QUOTE_MENTION_START_INDEX until tokenString.length)

        return mentionBuilder.createRoleMention(roleString)
    }
}
