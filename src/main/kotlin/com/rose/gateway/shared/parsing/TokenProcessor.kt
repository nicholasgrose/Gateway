package com.rose.gateway.shared.parsing

import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.TokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes an individual [Token], giving some result after processing it
 *
 * @param ResultType The type of the result that is returned after processing a token
 * @param AdditionalDataType The type that represents any additional data that will be needed for processing
 * @constructor Creates a token processor
 *
 * @see Token
 * @see TokenType
 * @see TextProcessor
 */
interface TokenProcessor<ResultType, AdditionalDataType> {
    /**
     * Gives the [TokenType] that this processor defines
     *
     * @return The [TokenType] defined
     *
     * @see TokenType
     * @see regexPattern
     */
    fun tokenType(): TokenType

    /**
     * Gives the regex that the defined [TokenType] is represented by in the text
     *
     * @return The regex in the form of a string
     *
     * @see TokenType
     * @see tokenType
     */
    @Language("RegExp")
    fun regexPattern(): String

    /**
     * Processes a [Token] in conjunction with data from the [AdditionalDataType] to get a result
     *
     * @param token The token to process
     * @param additionalData The additional data used for processing
     * @return The result of processing
     *
     * @see Token
     */
    suspend fun process(
        token: Token,
        additionalData: AdditionalDataType,
    ): ResultType
}
