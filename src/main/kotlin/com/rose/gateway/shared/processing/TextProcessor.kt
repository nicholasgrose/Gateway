package com.rose.gateway.shared.processing

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches

class TextProcessor<T>(private val processors: List<TokenProcessor<T>>) {
    private val tokenProcessorMap = processors.associateBy { it.tokenType() }

    private val lexer = lixy {
        state {
            for (processor in processors) {
                matches(processor.regexPattern()) isToken processor.tokenType()
            }
        }
    }

    suspend fun parseText(text: String): List<T> {
        val tokens = lexer.tokenize(text)

        return tokens.map { token -> processorFor(token).process(token) }
    }

    private fun processorFor(token: LixyToken): TokenProcessor<T> {
        return tokenProcessorMap[token.tokenType]!!
    }
}
