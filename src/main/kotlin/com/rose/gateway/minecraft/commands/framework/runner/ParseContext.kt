package com.rose.gateway.minecraft.commands.framework.runner

data class ParseContext<A : RunnerArguments<A>>(
    val arguments: A,
    val currentIndex: Int
)
