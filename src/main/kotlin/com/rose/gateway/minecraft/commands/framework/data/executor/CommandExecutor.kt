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
 * Represents a command executor that is responsible for parsing and executing commands.
 *
 * @param T The type of the result from parsing arguments
 * @param P The type of the ArgParser used for parsing arguments
 * @param B The type of the ParserBuilder used for building the ArgParser
 * @property executor The function that executes the command in a specific context
 * @property parser The ArgParser used for parsing arguments
 * @property confidenceModifier The function that modifies the confidence level of the executor
 */
data class CommandExecutor<T, P, B>(
    val executor: ((CommandExecuteContext) -> Boolean),
    val parser: ArgParser<T, P, B>,
    val confidenceModifier: (FrameworkContext<*>) -> Int = { 0 },
) where P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
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
     * @param context The context that in which the args will be parsed
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
    private fun remainingArgs(parseContext: ParseContext): List<String> {
        val rawArgs = parseContext.args.raw
        val lastIndex = parseContext.currentIndex

        return rawArgs.subList(lastIndex, rawArgs.size)
    }

    /**
     * Calculates the confidence level of that this executor should be used given the framework context.
     *
     * @param context The framework context to calculate confidence for.
     * @return The confidence level. Returns 1 = confidence modifier, if any, otherwise returns 0.
     */
    fun confidence(context: FrameworkContext<*>): Int {
        val parseResult = parseArg(context)

        return when (parseResult) {
            is ParseResult.Success -> 1 + confidenceModifier(context)
            is ParseResult.Failure -> 0
        }
    }
}
