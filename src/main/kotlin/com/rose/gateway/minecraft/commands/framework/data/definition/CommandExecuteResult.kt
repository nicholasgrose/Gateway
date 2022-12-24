package com.rose.gateway.minecraft.commands.framework.data.definition

import com.rose.gateway.minecraft.commands.framework.data.executor.ExecutorArgsPair

data class CommandExecuteResult(
    val succeeded: Boolean,
    val rankedExecutors: List<ExecutorArgsPair<*>>
)
