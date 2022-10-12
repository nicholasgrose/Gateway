package com.rose.gateway.minecraft.commands.framework.runner

sealed class ParseResult<T, A : CommandArgs<A>>(val context: ParseContext<A>) {
    class Success<T, A : CommandArgs<A>>(val result: T, context: ParseContext<A>) : ParseResult<T, A>(context)
    class Failure<T, A : CommandArgs<A>>(context: ParseContext<A>) : ParseResult<T, A>(context)
}
