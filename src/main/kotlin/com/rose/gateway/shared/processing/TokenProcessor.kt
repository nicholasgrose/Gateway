package com.rose.gateway.shared.processing

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType

interface TokenProcessor<T> {
    fun tokenType(): LixyTokenType
    fun regexPattern(): String
    suspend fun process(token: LixyToken): T
}
