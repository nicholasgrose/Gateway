package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import guru.zoroark.lixy.LixyToken

interface ChatTokenProcessor {
    fun regexPattern(): String
    suspend fun processToken(token: LixyToken): TokenProcessingResult
}
