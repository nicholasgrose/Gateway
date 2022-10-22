package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.emptyUsageGenerator
import com.rose.gateway.minecraft.commands.parsers.ProcessorParser
import com.rose.gateway.minecraft.commands.parsers.StringParser
import com.rose.gateway.minecraft.commands.parsers.list
import com.rose.gateway.minecraft.commands.parsers.processor
import com.rose.gateway.minecraft.commands.parsers.string
import com.rose.gateway.minecraft.commands.parsers.stringParser

/**
 * The arguments for a subcommand
 *
 * @property children The possible child subcommands
 * @constructor Create subcommand args
 */
class SubcommandArgs(private val children: Map<String, Command>) : CommandArgs<SubcommandArgs>() {
    companion object {
        /**
         * Creates subcommand args constructor for a set of child commands
         *
         * @param children The children to create the subcommand args for
         * @return The subcommand args constructor
         */
        fun forChildCommands(children: Map<String, Command>): () -> SubcommandArgs {
            return {
                SubcommandArgs(children)
            }
        }
    }

    private val subcommandName by string {
        name = "subcommand"
        description = "The subcommand to run."
        completer = ::subcommandCompleter
        validator = ::subcommandValidator
        usageGenerator = ::subcommandUsageGenerator
    }
    val subcommand by processor {
        name = "Next Command"
        description = "Handles status of next executor"
        processor = {
            val name = it.arguments.subcommandName
            val command = children[name]

            if (command != null) ParseResult.Success(command, it)
            else ParseResult.Failure(it)
        }
        usageGenerator = emptyUsageGenerator()
    }
    val remainingArgs by list {
        element = stringParser {
            name = "Remaining Arg"
            description = "One of the args remaining."
        }
        name = "Remaining Args"
        description = "All of the args to be passed to subcommands."
        usageGenerator = emptyUsageGenerator()
        completer = ::remainingArgsCompleter
        requireNonEmpty = false
    }

    @Suppress("unused")
    val nextCommand by processor {
        name = "Next Command"
        description = "Handles status of next executor"
        processor = ::nextCommandProcessor
        usageGenerator = ::nextCommandUsageGenerator
        completer = ::remainingArgsCompleter
        completesAfterSatisfied = true
    }

    /**
     * Tab completer for a subcommand name
     *
     * @param context The context around this tab completion
     * @return The possible completions for the input
     */
    @Suppress("UNUSED_PARAMETER")
    private fun subcommandCompleter(context: TabCompletionContext<SubcommandArgs>): List<String> {
        return children.keys.toList()
    }

    /**
     * Validates that the name is a valid child command
     *
     * @param result The result of parsing
     * @return Whether this subcommand has a valid name
     */
    private fun subcommandValidator(
        result: ParseResult.Success<String, SubcommandArgs>
    ): Boolean {
        return children.containsKey(result.result)
    }

    /**
     * Generates the possible usages for the subcommand's name
     *
     * @param args The args at the time of generating usage
     * @param arg The arg that the usages are being generated for
     * @return The possible usages for this argument
     */
    @Suppress("UNUSED_PARAMETER")
    private fun subcommandUsageGenerator(args: SubcommandArgs, arg: StringParser<SubcommandArgs>): List<String> {
        val subcommand = args.subcommandName

        return if (subcommand == null) children.keys.toList() else listOf(subcommand)
    }

    /**
     * Gives completions for the remaining arguments of a subcommand
     *
     * @param context The context at the moment of tab completion
     * @return The possible tab completions
     */
    private fun remainingArgsCompleter(context: TabCompletionContext<SubcommandArgs>): List<String> {
        val args = context.args
        val subcommand = args.subcommand
        val remainingRawArgs = args.remainingArgs ?: return listOf()

        return if (remainingRawArgs.isEmpty()) listOf()
        else subcommand?.onTabComplete(
            sender = context.sender,
            command = context.command,
            alias = context.alias,
            args = remainingRawArgs.toTypedArray()
        ) ?: listOf()
    }

    /**
     * Next command processor
     *
     * @param context
     * @return
     */
    private fun nextCommandProcessor(
        context: ParseContext<SubcommandArgs>
    ): ParseResult<Unit, SubcommandArgs> {
        val args = context.arguments
        val remainingArgs = args.remainingArgs ?: return ParseResult.Failure(context)
        val subcommand = args.subcommand

        val succeeded = subcommand?.definition?.executors?.any {
            it.arguments(remainingArgs.toTypedArray()).valid()
        } ?: false

        return if (succeeded) ParseResult.Success(Unit, context)
        else ParseResult.Failure(context)
    }

    /**
     * Generates usages for the subcommand that would execute next
     *
     * @param args The args at the moment of generating these usages
     * @param arg The arg to generate the usages for
     * @return The possible usages for the next command
     */
    @Suppress("UNUSED_PARAMETER")
    private fun nextCommandUsageGenerator(
        args: SubcommandArgs,
        arg: ProcessorParser<Unit, SubcommandArgs>
    ): List<String> {
        val remainingRawArgs = args.remainingArgs ?: return listOf()
        val subcommand = args.subcommand

        return if (subcommand == null) listOf()
        else {
            val executor = subcommand.definition.executors.firstOrNull()
            val subcommandDocs = executor?.arguments(remainingRawArgs.toTypedArray())?.usages()

            subcommandDocs ?: listOf()
        }
    }
}
