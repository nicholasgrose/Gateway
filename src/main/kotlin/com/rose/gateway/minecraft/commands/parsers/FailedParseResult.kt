package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult

/**
 * Creates a parse result that represents a failure in the given context
 *
 * @param T The type that should have been parsed
 * @param A The args this parse result was generated in
 * @param context The context this parse result is in
 * @return The constructed parse result
 */
fun <T, A : CommandArgs<A>> failedParseResult(context: ParseContext<A>): ParseResult<T, A> = ParseResult(
    succeeded = false,
    result = null,
    context = context
)
