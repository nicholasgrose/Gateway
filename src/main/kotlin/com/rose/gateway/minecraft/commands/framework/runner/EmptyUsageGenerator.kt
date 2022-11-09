package com.rose.gateway.minecraft.commands.framework.runner

/**
 * Provides a usage generator that returns no usages
 *
 * @param T The type of the argument's value
 * @param A The type of the args the argument is a part of
 * @param P The type of the parser for the argument the result provides usages for
 * @return A usage generator that always returns an empty set of usages
 */
fun <T, A : CommandArgs<A>, P : ArgParser<T, A, P>> emptyUsageGenerator(): (P) -> List<String> = { _ ->
    listOf()
}
