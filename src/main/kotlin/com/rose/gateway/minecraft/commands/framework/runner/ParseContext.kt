package com.rose.gateway.minecraft.commands.framework.runner

data class ParseContext<A : CommandArgs<A>>(
    val arguments: A,
    val currentIndex: Int
)
