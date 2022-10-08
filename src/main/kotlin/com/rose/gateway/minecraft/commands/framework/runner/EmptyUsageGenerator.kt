package com.rose.gateway.minecraft.commands.framework.runner

fun <
    T,
    A : CommandArgs<A>,
    P : ArgParser<T, A, P>
    > emptyUsageGenerator(): (A, P) -> List<String> {
    return { _, _ ->
        listOf()
    }
}
