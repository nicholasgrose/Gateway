package com.rose.gateway.minecraft.commands.framework.data

data class CommandResult(
    val succeeded: Boolean,
    val rankedExecutors: List<ExecutorArgsPair<*>>
)
