package com.rose.gateway.shared.parsing

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes an individual [LixyToken], giving some result after processing it.
 *
 * @param ResultType The type of the result that is returned after processing a token.
 * @param AdditionalDataType The type that represents any additional data that will be needed for processing.
 * @constructor Creates a token processor.
 *
 * @see LixyToken
 * @see LixyTokenType
 * @see TextProcessor
 */
interface TokenProcessor<ResultType, AdditionalDataType> {
    /**
     * Gives the [LixyTokenType] that this processor defines.
     *
     * @return The [LixyTokenType] defined.
     *
     * @see LixyTokenType
     * @see regexPattern
     */
    fun tokenType(): LixyTokenType

    /**
     * Gives the regex that the defined [LixyTokenType] is represented by in the text.
     *
     * @return The regex in the form of a string.
     *
     * @see LixyTokenType
     * @see tokenType
     */
    @Language("RegExp")
    fun regexPattern(): String

    /**
     * Processes a [LixyToken] in conjunction with data from the [AdditionalDataType] to get a result.
     *
     * @param token The token to process.
     * @param additionalData The additional data used for processing.
     * @return The result of processing.
     *
     * @see LixyToken
     */
    suspend fun process(token: LixyToken, additionalData: AdditionalDataType): ResultType
}
