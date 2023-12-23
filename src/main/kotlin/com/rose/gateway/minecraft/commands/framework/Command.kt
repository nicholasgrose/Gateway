package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.FrameworkContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandExecutionResult
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor

/**
 * A command that can execute and provide completions
 *
 * @property definition This command's definition
 * @constructor Create a command
 */
class Command(val definition: CommandBuilder) {
    /**
     * Parses the arguments of a context and then executes on the result
     *
     * @param context The context to execute in
     * @return The result of execution
     */
    fun parseAndExecute(context: CommandExecuteContext): CommandExecutionResult {
        val executors = bestExecutors(context)

        for (executor in executors) {
            val executeResult = executor.parseAndExecute(context)

            if (executeResult == true) {
                return CommandExecutionResult.Successful()
            }
        }

        return CommandExecutionResult.Failed(context, executors)
    }

    /**
     * Returns the list of best CommandExecutors based on confidence level
     *
     * @param context The FrameworkContext representing the context of an action for a command
     * @return The list of best CommandExecutors
     */
    fun bestExecutors(context: FrameworkContext<*>): List<CommandExecutor<*, *, *>> {
        var maxConfidence = 0
        var topRankExecutors = mutableListOf<CommandExecutor<*, *, *>>()

        for (executor in definition.executors) {
            val confidence = executor.confidence(context)

            when {
                confidence == maxConfidence -> topRankExecutors.add(executor)
                confidence > maxConfidence -> {
                    topRankExecutors = mutableListOf(executor)
                    maxConfidence = confidence
                }
            }
        }

        return topRankExecutors
    }

    /**
     * Parses the arguments of a context and then completes for the result
     *
     * @param context The context to complete for
     * @return Possible completions for the context
     */
    fun parseAndComplete(context: TabCompleteContext): List<String> {
        val executors = bestExecutors(context)

        return complete(context, executors)
    }

    /**
     * Completes for a context given the ranked executors
     *
     * @param context The context to complete for
     * @param executors The ranked executors with their parsed args
     * @return Possible completions for the context
     */
    fun complete(context: TabCompleteContext, executors: List<CommandExecutor<*, *, *>>): List<String> {
        val tabCompletions = executors.map { it.completions(context) }.flatten()

        return tabCompletions
    }

    /**
     * Determines which executors are considered the most successful
     *
     * Success is defined as either being successful or having the most arguments successfully parsed.
     * The returned executors are in the same order they were defined.
     *
     * @param context The context containing the incoming arguments to be parsed
     * @return List of executors in order of definition
     */
    fun mostSuccessfulExecutor(context: FrameworkContext<*>): CommandExecutor<*, *, *>? {
        return definition.executors.firstOrNull { executor ->
            executor.parseArg(context) is ParseResult.Success
        }
    }
}
