package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandExecuteResult
import com.rose.gateway.minecraft.commands.framework.data.executor.ExecutorArgsPair

class Command(val definition: CommandDefinition) {
    fun parseAndExecute(context: CommandExecuteContext<*>): CommandExecuteResult {
        val mostSuccessfulExecutors = mostSuccessfulExecutors(context.args.rawArguments)

        return execute(context, mostSuccessfulExecutors)
    }

    fun execute(context: CommandExecuteContext<*>, executorArgsPairs: List<ExecutorArgsPair<*>>): CommandExecuteResult {
        val chosenPair = executorArgsPairs.firstOrNull()

        val succeeded = if (chosenPair == null) {
            false
        } else {
            chosenPair.executor.tryExecute(context) ?: false
        }

        return CommandExecuteResult(succeeded, executorArgsPairs)
    }

    fun parseAndComplete(context: TabCompleteContext<*>): List<String> {
        val mostSuccessfulExecutors = mostSuccessfulExecutors(context.args.rawArguments)

        return complete(context, mostSuccessfulExecutors)
    }

    fun complete(context: TabCompleteContext<*>, executorArgsPairs: List<ExecutorArgsPair<*>>): List<String> {
        val tabCompletions = executorArgsPairs.map {
            it.executor.completions(context)
        }.flatten()

        return tabCompletions
    }

    /**
     * Determines which executors are considered the most successful
     *
     * Success is defined as either being successful or having the most arguments successfully parsed.
     * The returned executors are in the same order they were defined.
     *
     * @param rawArgs The incoming arguments to be parsed
     * @return List of executors in order of definition
     */
    fun mostSuccessfulExecutors(rawArgs: List<String>): List<ExecutorArgsPair<*>> {
        val mostSuccessfulExecutors = mutableListOf<ExecutorArgsPair<*>>()
        var successThreshold = 0
        var executorsMustBeSuccessful = false

        for (executor in definition.executors) {
            val executorArgsPair = ExecutorArgsPair.forExecutor(executor, rawArgs)
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
