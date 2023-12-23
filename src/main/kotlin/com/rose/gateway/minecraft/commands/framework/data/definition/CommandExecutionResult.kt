package com.rose.gateway.minecraft.commands.framework.data.definition

import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor

/**
 * Command execution result
 *
 * @constructor Create empty Command execution result
 */
sealed class CommandExecutionResult {
    /**
     * Class representing a successful command execution result.
     *
     * @constructor Create an instance of the Successful class.
     */
    class Successful : CommandExecutionResult()

    /**
     * Represents a failed command execution result.
     *
     * @property context The command execute context.
     * @property bestExecutors The list of best command executors.
     */
    class Failed(val context: CommandExecuteContext, val bestExecutors: List<CommandExecutor<*, *, *>>) :
        CommandExecutionResult()
}
