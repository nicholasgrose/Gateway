package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult

fun <T, A : CommandArgs<A>> failedParseResult(context: ParseContext<A>): ParseResult<T, A> = ParseResult(
    succeeded = false,
    result = null,
    context = context
)
