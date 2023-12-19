package com.rose.gateway.minecraft.commands.framework.data.definition

import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor

sealed class CommandExecutionResult {
    class Successful : CommandExecutionResult()
    class Failed(val context: CommandExecuteContext, val bestExecutors: List<CommandExecutor<*, *, *>>) :
        CommandExecutionResult()
}
