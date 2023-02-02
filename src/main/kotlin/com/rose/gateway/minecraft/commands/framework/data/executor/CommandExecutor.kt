package com.rose.gateway.minecraft.commands.framework.data.executor

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.FrameworkContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.shared.collections.builders.trieOf

/**
 * An executor for a command
 *
 * @param A The type of the args that will be used for execution
 * @property executor The action that this executor defines
 * @property argsRef The reference this executor will use when  adding its args to the context
 * @constructor Create a command executor
 */
data class CommandExecutor<A : CommandArgs<A>>(
    val executor: ((CommandExecuteContext) -> Boolean),
    val argsRef: ArgsReference<A>
) {
    /**
     * Attempts to execute the executor in a particular context
     *
     * @param context The context to execute in
     * @return Whether execution succeeded or null if it never executed
     */
    fun tryExecute(context: CommandExecuteContext): Boolean? {
        val args = fillArgs(context)

        return if (args.valid()) {
            executor(context)
        } else null
    }

    /**
     * Constructs filled-in arguments for the given raw arguments
     *
     * @param argsContext The context that in which the args will be parsed
     * @return The constructed arguments
     */
    fun fillArgs(context: FrameworkContext<*>): A {
        val args = argsRef.args().parseArguments(argsRef, context)
        context.args.parsed[argsRef] = args

        return args
    }

    /**
     * Provides a list of possible completions for a particular context
     *
     * @param context The context in which completions are provided
     * @return The possible completions for tab
     */
    fun completions(context: TabCompleteContext<A>): List<String> {
        val args = fillArgs(context)
        val remainingArgs = args.remainingArgs().joinToString(" ")
        val completions = args.completions(argsRef, context)

        return trieOf(completions).search(remainingArgs)
    }
}
