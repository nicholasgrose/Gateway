package com.rose.gateway.minecraft.commands.framework.data.executor

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.FrameworkContext
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.shared.collections.builders.trieOf

/**
 * An executor for a command
 *
 * @param A The type of the args that will be used for execution
 * @property executor The action that this executor defines
 * @property parser The reference this executor will use when  adding its args to the context
 * @constructor Create a command executor
 */
data class CommandExecutor<T, P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B>>(
    val executor: ((CommandExecuteContext) -> Boolean),
    val parser: ArgParser<T, P, B>,
    val confidenceModifier: (FrameworkContext<*>) -> Int = { 0 },
) {
    /**
     * Attempts to execute the executor in a particular context
     *
     * @param context The context to execute in
     * @return Whether execution succeeded or null if it never executed
     */
    fun parseAndExecute(context: CommandExecuteContext): Boolean? {
        val parseResult = parseArg(context)

        return when (parseResult) {
            is ParseResult.Success -> executor(context)
            is ParseResult.Failure -> null
        }
    }

    /**
     * Constructs filled-in arguments for the given raw arguments
     *
     * @param argsContext The context that in which the args will be parsed
     * @return The constructed arguments
     */
    fun parseArg(context: FrameworkContext<*>): ParseResult<T> {
        val parsedValue = parser.parseValidValue(
            ParseContext(
                context.command,
                context.args,
                context.bukkit,
                0,
            ),
        )
        context.args.parsed[parser] = parsedValue

        return parsedValue
    }

    /**
     * Provides a list of possible completions for a particular context
     *
     * @param context The context in which completions are provided
     * @return The possible completions for tab
     */
    fun completions(context: TabCompleteContext): List<String> {
        val parseResult = parseArg(context)
        val remainingArgs = remainingArgs(parseResult.context).joinToString(" ")
        val completions = parser.completions(context)

        return trieOf(completions).search(remainingArgs)
    }

    /**
     * Retrieves all the args remaining from amongst the raw args
     *
     * @param parseContext The context to pull the args from
     * @return The remaining args
     */
    fun remainingArgs(parseContext: ParseContext): List<String> {
        val rawArgs = parseContext.args.raw
        val lastIndex = parseContext.currentIndex

        return rawArgs.subList(lastIndex, rawArgs.size)
    }

    fun confidence(context: FrameworkContext<*>): Int {
        val parseResult = parseArg(context)

        return when (parseResult) {
            is ParseResult.Success -> 1 + confidenceModifier(context)
            is ParseResult.Failure -> 0
        }
    }
}
