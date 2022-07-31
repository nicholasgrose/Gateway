package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.converters.ProcessorArg
import com.rose.gateway.minecraft.commands.converters.StringArg
import com.rose.gateway.minecraft.commands.converters.failedParseResult
import com.rose.gateway.minecraft.commands.converters.list
import com.rose.gateway.minecraft.commands.converters.processor
import com.rose.gateway.minecraft.commands.converters.string
import com.rose.gateway.minecraft.commands.converters.stringArg
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

class SubcommandArguments(private val children: Map<String, Command>) : RunnerArguments<SubcommandArguments>() {
    private val validator = Regex(subcommandRegex())

    private fun subcommandRegex(): String {
        return children.keys.joinToString("|", "^(", ")$")
    }

    companion object {
        fun forChildCommands(children: Map<String, Command>): () -> SubcommandArguments {
            return {
                SubcommandArguments(children)
            }
        }
    }

    val subcommand by string {
        name = "subcommand"
        description = "The subcommand to run."
        completer = ::subcommandCompleter
        validator = ::subcommandValidator
        docGenerator = ::subcommandDocGenerator
    }
    val remainingArgs by list {
        element = stringArg {
            name = "Remaining Arg"
            description = "One of the args remaining."
        }
        name = "Remaining Args"
        description = "All of the args to be passed to subcommands."
    }

    @Suppress("unused")
    val nextCommand by processor {
        name = "Next Command"
        description = "Handles status of next executor"
        processor = ::nextCommandProcessor
        completer = ::nextCommandCompleter
        docGenerator = ::nextCommandDocGenerator
    }

    private fun subcommandCompleter(context: TabCompletionContext<SubcommandArguments>): List<String> {
        val command = context.arguments.rawArguments.first()
        val definition = context.commandDefinition

        return definition.subcommandNames.searchOrGetAll(command)
    }

    private fun subcommandValidator(
        result: ParseResult<String, SubcommandArguments>
    ): Boolean {
        result.result ?: return false

        return validator.matches(result.result)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun subcommandDocGenerator(args: SubcommandArguments, arg: StringArg<SubcommandArguments>): String {
        return args.subcommand
            ?: children.keys.joinToString(
                separator = " | ",
                prefix = "[",
                postfix = "]"
            )
    }

    private fun nextCommandCompleter(context: TabCompletionContext<SubcommandArguments>): List<String> {
        val args = context.arguments
        val subcommandName = args.subcommand!!
        val subcommand = children[subcommandName]
        val remainingRawArgs = args.remainingArgs ?: return listOf()

        return if (remainingRawArgs.isEmpty()) listOf()
        else subcommand?.onTabComplete(
            sender = context.sender,
            command = context.command,
            alias = context.alias,
            args = remainingRawArgs.toTypedArray()
        ) ?: listOf()
    }

    private fun nextCommandProcessor(
        context: ParseContext<SubcommandArguments>
    ): ParseResult<Unit, SubcommandArguments> {
        val args = context.arguments
        val remainingArgs = args.remainingArgs ?: return failedParseResult(context)

        val subcommandName = args.subcommand
        val subcommand = children[subcommandName]

        val succeeded = subcommand?.definition?.executors?.any {
            it.arguments(remainingArgs.toTypedArray()).valid()
        } ?: false

        return ParseResult(
            succeeded = succeeded,
            result = Unit,
            context = context
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun nextCommandDocGenerator(
        args: SubcommandArguments,
        arg: ProcessorArg<Unit, SubcommandArguments>
    ): String {
        val remainingRawArgs = args.remainingArgs ?: return "no additional args found for docs"
        val subcommandName = args.subcommand
        val subcommand = children[subcommandName]

        return if (subcommand == null) ""
        else {
            val executor = subcommand.definition.executors.firstOrNull()

            executor?.arguments(remainingRawArgs.toTypedArray())?.documentation() ?: "no executor found for docs"
        }
    }
}

fun subcommandRunner(context: CommandContext<SubcommandArguments>): Boolean {
    val childCommand = context.definition.subcommands[context.arguments.subcommand]
    val remainingArguments = context.arguments.remainingArgs?.toTypedArray()

    return if (childCommand == null || remainingArguments == null) false
    else {
        childCommand.onCommand(
            sender = context.sender,
            command = context.command,
            label = context.label,
            args = remainingArguments
        )
        true
    }
}
