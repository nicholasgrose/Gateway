package com.rose.gateway.minecraft.commands.framework.data.definition

import com.rose.gateway.minecraft.commands.framework.data.executor.ExecutorArgsPair

/**
 * The result of a command's execution
 *
 * @property succeeded Whether command execution succeeded
 * @property rankedExecutors The ranked ordering it assigned to its executors
 * @constructor Create a command execution result
 */
data class CommandExecuteResult(
    val succeeded: Boolean,
    val rankedExecutors: List<ExecutorArgsPair<*>>,
)
