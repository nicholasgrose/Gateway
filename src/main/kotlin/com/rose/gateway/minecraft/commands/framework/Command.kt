package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.FrameworkContext
import com.rose.gateway.minecraft.commands.framework.data.context.ParserSpecificContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandExecuteResult
import com.rose.gateway.minecraft.commands.framework.data.executor.ExecutorArgsPair
import com.rose.gateway.minecraft.commands.parsers.UnitParser

/**
 * A command that can execute and provide completions
 *
 * @property definition This command's definition
 * @constructor Create a command
 */
class Command(val definition: CommandDefinition) {
    /**
     * Parses the arguments of a context and then executes on the result
     *
     * @param context The context to execute in
     * @return The result of execution
     */
    fun parseAndExecute(context: CommandExecuteContext): CommandExecuteResult {
        val mostSuccessfulExecutors = mostSuccessfulExecutors(context)

        return execute(context, mostSuccessfulExecutors)
    }

    /**
     * Executes in a context given the ranked executors
     *
     * @param context The context to execute in
     * @param executorArgsPairs The ranked executors with their parsed args
     * @return The result of execution
     */
    fun execute(context: CommandExecuteContext, executorArgsPairs: List<ExecutorArgsPair<*>>): CommandExecuteResult {
        val chosenPair = executorArgsPairs.firstOrNull()

        val succeeded = if (chosenPair == null) {
            false
        } else {
            chosenPair.executor.tryExecute(context) ?: false
        }

        return CommandExecuteResult(succeeded, executorArgsPairs)
    }

    /**
     * Parses the arguments of a context and then completes for the result
     *
     * @param context The context to complete for
     * @return Possible completions for the context
     */
    fun parseAndComplete(context: TabCompleteContext<*>): List<String> {
        val mostSuccessfulExecutors = mostSuccessfulExecutors(context)

        return complete(context, mostSuccessfulExecutors)
    }

    /**
     * Completes for a context given the ranked executors
     *
     * @param context The context to complete for
     * @param executorArgsPairs The ranked executors with their parsed args
     * @return Possible completions for the context
     */
    fun complete(context: TabCompleteContext<*>, executorArgsPairs: List<ExecutorArgsPair<*>>): List<String> {
        val tabCompletions = executorArgsPairs.map { pairCompletions(it, context) }.flatten()

        return tabCompletions
    }

    /**
     * Handles the computation of completions for an executor in a type-safe way
     *
     * @param A The type of the args in the pair
     * @param pair The pair to complete for
     * @param context The context the pair is completing within
     * @return The computed completions
     */
    private fun <A : CommandArgs<A>> pairCompletions(
        pair: ExecutorArgsPair<A>,
        context: TabCompleteContext<*>
    ): List<String> {
        return pair.executor.completions(
            TabCompleteContext(
                context.command,
                context.args,
                context.bukkit,
                ParserSpecificContext(
                    pair.executor.argsRef,
                    UnitParser()
                )
            )
        )
    }

    /**
     * Determines which executors are considered the most successful
     *
     * Success is defined as either being successful or having the most arguments successfully parsed.
     * The returned executors are in the same order they were defined.
     *
     * @param args The incoming arguments to be parsed
     * @return List of executors in order of definition
     */
    fun mostSuccessfulExecutors(args: FrameworkContext<*>): List<ExecutorArgsPair<*>> {
        val mostSuccessfulExecutors = mutableListOf<ExecutorArgsPair<*>>()
        var successThreshold = 0
        var executorsMustBeSuccessful = false

        for (executor in definition.executors) {
            val executorArgsPair = ExecutorArgsPair.forExecutor(executor, args)
            val argResult = executorArgsPair.args
            val argsParsed = argResult.argsParsed()

            when {
                argResult.valid() -> {
                    if (!executorsMustBeSuccessful) {
                        mostSuccessfulExecutors.clear()
                        executorsMustBeSuccessful = true
                    }

                    mostSuccessfulExecutors.add(executorArgsPair)
                }

                executorsMustBeSuccessful && !argResult.valid() -> continue
                argsParsed == successThreshold -> mostSuccessfulExecutors.add(executorArgsPair)
                argsParsed > successThreshold -> {
                    successThreshold = argsParsed
                    mostSuccessfulExecutors.clear()
                    mostSuccessfulExecutors.add(executorArgsPair)
                }
            }
        }

        return mostSuccessfulExecutors
    }
}
