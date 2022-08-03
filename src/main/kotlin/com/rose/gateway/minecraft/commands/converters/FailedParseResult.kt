package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

fun <T, A : RunnerArguments<A>> failedParseResult(context: ParseContext<A>): ParseResult<T, A> = ParseResult(
    succeeded = false,
    result = null,
    context = context
)
