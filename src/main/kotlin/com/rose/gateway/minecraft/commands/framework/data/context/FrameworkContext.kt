package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder

/**
 * The context of an action for a command
 *
 * @param B The type of the bukkit context
 * @property command The command in use
 * @property args Data about the args in this context
 * @property bukkit The context of the bukkit command
 * @constructor Create a framework context
 */
public sealed class FrameworkContext<B : BukkitContext>(
    val command: Command,
    val args: ArgsContext,
    val bukkit: B,
) {
    /**
     * The context of executing a command
     *
     * @constructor Create a command execute context
     *
     * @param command The command in use
     * @param args Data about the args in this context
     * @param bukkit The context of the bukkit command
     */
    class CommandExecute(
        command: Command,
        args: ArgsContext,
        bukkit: BukkitContext.CommandExecute,
    ) : FrameworkContext<BukkitContext.CommandExecute>(command, args, bukkit)

    /**
     * The context around parsing a command
     *
     * @param A The type of the args in the context
     * @property currentIndex The next index to use from the raw args
     * @constructor Create a parse command arg context
     *
     * @param command The command in use
     * @param args Data about the args in this context
     * @param parser The context of the parser current being used
     * @param bukkit The context of the bukkit command
     */
    class TabComplete(
        command: Command,
        args: ArgsContext,
        bukkit: BukkitContext.TabComplete,
    ) : FrameworkContext<BukkitContext.TabComplete>(command, args, bukkit)

    /**
     * The context around parsing a command
     *
     * @param A The type of the args in the context
     * @property currentIndex The next index to use from the raw args
     * @constructor Create a parse command arg context
     *
     * @param command The command in use
     * @param args Data about the args in this context
     * @param parser The context of the parser current being used
     * @param bukkit The context of the bukkit command
     */
    class Parse(
        command: Command,
        args: ArgsContext,
        bukkit: BukkitContext,
        val currentIndex: Int,
    ) : FrameworkContext<BukkitContext>(command, args, bukkit)

    /**
     * Gives the result for an args reference and casts them to the correct type
     *
     * @param argsRef The reference to the args
     * @return The referenced args
     */
    fun <T, P, B> resultOf(
        argParser: ArgParser<T, P, B>,
    ): ParseResult<T>?
            where P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
        val parseResult = args.parsed[argParser] ?: return null

        @Suppress("UNCHECKED_CAST")
        return parseResult as ParseResult<T>
    }

    /**
     * Gives the result for an args reference and casts them to the correct type
     *
     * @param argsRef The reference to the args
     * @return The referenced args
     */
    fun <T, P, B> resultOfOrDefault(
        argParser: ArgParser<T, P, B>,
    ): ParseResult<T>
            where P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
        return resultOf(argParser) ?: return ParseResult.Failure(
            ParseContext(
                command,
                args,
                bukkit,
                0,
            ),
        )
    }

    /**
     * Gives the value for an args reference and casts it to the correct type
     *
     * @param argParser The reference to the args
     * @return The referenced args
     */
    fun <T, P, B> valueOf(
        argParser: ArgParser<T, P, B>,
    ): T
            where P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
        val parseResult = resultOf(argParser)

        return when (parseResult) {
            null -> error("Asserted that result for $argParser exists but it doesn't")
            is ParseResult.Failure -> error("Asserted that result for $argParser was successful but it wasn't")
            is ParseResult.Success -> parseResult.value
        }
    }

    /**
     * Gives the value for the args reference to the parser and casts it to the correct type
     *
     * @param argParser The arg parser to get the parsed value for
     * @return The value acquired or null if it has no successful result
     */
    fun <T, P, B> valueOrNullOf(
        argParser: ArgParser<T, P, B>,
    ): T?
            where P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
        val parseResult = resultOf(argParser)

        return when (parseResult) {
            null -> null
            is ParseResult.Failure -> null
            is ParseResult.Success -> parseResult.value
        }
    }
}

/**
 * The context for command execution
 */
typealias CommandExecuteContext = FrameworkContext.CommandExecute

/**
 * The context for completing a command argument
 */
typealias TabCompleteContext = FrameworkContext.TabComplete

/**
 * The context for parsing a command argument
 */
typealias ParseContext = FrameworkContext.Parse
