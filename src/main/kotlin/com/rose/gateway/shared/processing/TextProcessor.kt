package com.rose.gateway.shared.processing

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches

class TextProcessor<T, A>(private val processors: List<TokenProcessor<T, A>>) {
    private val tokenProcessorMap = processors.associateBy { it.tokenType() }

    private val lexer = lixy {
        state {
            for (processor in processors) {
                matches(processor.regexPattern()) isToken processor.tokenType()
            }
        }
    }

    suspend fun parseText(text: String, additionalData: A): List<T> {
        val tokens = lexer.tokenize(text)

        return tokens.map { token -> processorFor(token).process(token, additionalData) }
    }

    private fun processorFor(token: LixyToken): TokenProcessor<T, A> {
        return tokenProcessorMap[token.tokenType]!!
    }
}
