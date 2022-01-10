package com.rose.gateway.shared.processing

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches

class TextProcessor<T>(private val processors: Map<LixyTokenType, ChatTokenProcessor<T>>) {
    private val lexer = lixy {
        state {
            for ((token, processor) in processors) {
                matches(processor.regexPattern()) isToken token
            }
        }
    }

    suspend fun parseText(text: String): List<T> {
        val tokens = lexer.tokenize(text)

        return tokens.map { token -> processorFor(token).process(token) }
    }

    private fun processorFor(token: LixyToken): ChatTokenProcessor<T> {
        return processors[token.tokenType]!!
    }
}
