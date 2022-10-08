package com.rose.gateway.minecraft.commands.framework.runner

data class ParseResult<T, A : CommandArgs<A>>(
    val succeeded: Boolean,
    val result: T?,
    val context: ParseContext<A>
)
