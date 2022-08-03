package com.rose.gateway.minecraft.commands.framework.runner

fun <T, A : RunnerArguments<A>, R : RunnerArg<T, A, R>> emptyUsageGenerator(): (A, R) -> List<String> {
    return { _, _ ->
        listOf()
    }
}
