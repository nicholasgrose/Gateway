package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.parser.ParseContext

/**
 * The result of parsing a value
 *
 * @param T The type of the value to be parsed
 * @param A The type of the args this result is for
 * @property context The new context after parsing
 * @constructor Create a parse result
 */
sealed class ParseResult<T, A : CommandArgs<A>>(val context: ParseContext<A>) {
    /**
     * A successful parse result
     *
     * @param T The type of the value that was parsed
     * @param A The type of the args this result is for
     * @property result The value that was parsed
     * @constructor Create a successful parse result
     *
     * @param context The new context after parsing
     */
    class Success<T, A : CommandArgs<A>>(val result: T, context: ParseContext<A>) : ParseResult<T, A>(context)

    /**
     * A failed parse result
     *
     * @param T The type of the value that was supposed to be parsed
     * @param A The type of the args this result is for
     * @constructor Create a failed parse result
     *
     * @param context The new context after parsing
     */
    class Failure<T, A : CommandArgs<A>>(context: ParseContext<A>) : ParseResult<T, A>(context)
}
