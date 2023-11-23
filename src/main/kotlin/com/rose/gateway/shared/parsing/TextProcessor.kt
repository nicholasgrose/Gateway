package com.rose.gateway.shared.parsing

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches

/**
 * A processor that can takes text, parses it, and maps the result into the [ResultType]
 *
 * @param ResultType The type that each parsed token is mapped into
 * @param AdditionalDataType The additional data that is provided when a token is mapped
 * (e.g. Unit, Int, DataClass, etc.)
 * @constructor Creates a text processor defined by the provided [TokenProcessor]s
 *
 * @param processors The list of [TokenProcessor]s to use for defining and processing tokens
 *
 * @see TokenProcessor
 */
class TextProcessor<ResultType, AdditionalDataType>(processors: List<TokenProcessor<ResultType, AdditionalDataType>>) {
    private val tokenProcessorMap = processors.associateBy { it.tokenType() }
    private val lexer =
        lixy {
            state {
                // Defining the regex of each processor as matching that processor's token type.
                for (processor in processors) {
                    matches(processor.regexPattern()) isToken processor.tokenType()
                }
            }
        }

    /**
     * Breaks a string of text into a list of tokens to be mapped through their respective [TokenProcessor]
     *
     * @param text The text to parse and process
     * @param additionalData The additional data to be used for token processing
     * @return The parsed and processed text
     *
     * @see TokenProcessor
     * @see processorFor
     */
    suspend fun parseText(
        text: String,
        additionalData: AdditionalDataType,
    ): List<ResultType> {
        val tokens = lexer.tokenize(text)

        return tokens.map { token -> processorFor(token).process(token, additionalData) }
    }

    /**
     * Finds the [TokenProcessor] that handles a [LixyToken]'s type
     *
     * @param token The token to find the processor for
     * @return The processor that can handle the token's type
     *
     * @see LixyToken
     * @see guru.zoroark.lixy.LixyTokenType
     * @see TokenProcessor
     */
    private fun processorFor(token: LixyToken): TokenProcessor<ResultType, AdditionalDataType> {
        return tokenProcessorMap[token.tokenType]!!
    }
}
