package com.rose.gateway.shared.processing

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import org.intellij.lang.annotations.Language

interface TokenProcessor<T> {
    fun tokenType(): LixyTokenType
    @Language("RegExp")
    fun regexPattern(): String
    suspend fun process(token: LixyToken): T
}
