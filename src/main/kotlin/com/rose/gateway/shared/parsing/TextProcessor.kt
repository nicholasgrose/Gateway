package com.rose.gateway.shared.parsing

import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.matchers.matches
import guru.zoroark.tegral.niwen.lexer.niwenLexer

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
        niwenLexer {
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
     * Finds the [TokenProcessor] that handles a [Token]'s type
     *
     * @param token The token to find the processor for
     * @return The processor that can handle the token's type
     *
     * @see Token
     * @see guru.zoroark.lixy.TokenType
     * @see TokenProcessor
     */
    private fun processorFor(token: Token): TokenProcessor<ResultType, AdditionalDataType> =
        tokenProcessorMap[token.tokenType]!!
}
