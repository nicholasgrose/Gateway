package com.rose.gateway.minecraft.commands.framework.args

import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext

/**
 * The result of parsing a value
 *
 * @param T The type of the value to be parsed
 * @property context The new context after parsing
 * @constructor Create a parse result
 */
sealed class ParseResult<T>(val context: ParseContext) {
    /**
     * A successful parse result
     *
     * @param T The type of the value that was parsed
     * @property value The value that was parsed
     * @constructor Create a successful parse result
     *
     * @param context The new context after parsing
     */
    class Success<T>(val value: T, context: ParseContext) : ParseResult<T>(context)

    /**
     * A failed parse result
     *
     * @param T The type of the value that was supposed to be parsed
     * @constructor Create a failed parse result
     *
     * @param context The new context after parsing
     */
    class Failure<T>(context: ParseContext) : ParseResult<T>(context)
}
