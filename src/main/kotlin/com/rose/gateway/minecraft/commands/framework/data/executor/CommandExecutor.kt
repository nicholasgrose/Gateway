package com.rose.gateway.minecraft.commands.framework.data.executor

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.parsers.UnitParser
import com.rose.gateway.shared.collections.builders.trieOf

/**
 * An executor for a command
 *
 * @param A The type of the args that will be used for execution
 * @property executor The action that this executor defines
 * @property args A constructor for the arguments used by this executor
 * @constructor Create a command executor
 */
data class CommandExecutor<A : CommandArgs<A>>(
    val executor: ((CommandExecuteContext<A>) -> Boolean),
    val args: () -> A,
) {
    /**
     * Attempts to execute the executor in a particular context
     *
     * @param context The context to execute in
     * @return Whether execution succeeded or null if it never executed
     */
    fun tryExecute(context: CommandExecuteContext<*>): Boolean? {
        val args = filledArgs(context.args.rawArguments)

        return if (args.valid()) {
            executor(
                CommandExecuteContext(
                    bukkit = context.bukkit,
                    command = context.command,
                    args = args,
                ),
            )
        } else {
            null
        }
    }

    /**
     * Constructs filled-in arguments for the given raw arguments
     *
     * @param rawArguments The arguments that will be parsed
     * @return The constructed arguments
     */
    fun filledArgs(rawArguments: List<String>): A = args().parseArguments(rawArguments)

    /**
     * Provides a list of possible completions for a particular context
     *
     * @param context The context in which completions are provided
     * @return The possible completions for tab
     */
    fun completions(context: TabCompleteContext<*>): List<String> {
        val args = filledArgs(context.args.rawArguments)
        val remainingArgs = args.remainingArgs().joinToString(" ")
        val completions = args.completions(
            TabCompleteContext(
                bukkit = context.bukkit,
                command = context.command,
                args = args,
                completingParser = UnitParser(),
            ),
        )

        return trieOf(completions).search(remainingArgs)
    }
}
