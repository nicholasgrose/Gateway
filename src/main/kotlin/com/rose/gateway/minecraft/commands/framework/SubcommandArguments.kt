package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.converters.list
import com.rose.gateway.minecraft.commands.converters.string
import com.rose.gateway.minecraft.commands.converters.stringArg
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
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

    val subcommand: String? by string {
        name = "subcommand"
        description = "The subcommand to run."
        completer = ::subcommandCompleter
        validator = ::subcommandValidator
        docGenerator = ::subcommandDocGenerator
    }
    val remainingArgs: List<String>? by list {
        argType = stringArg {
            name = "Remaining Arg"
            description = "One of the args remaining."
        }
        name = "Remaining Args"
        description = "All of the args to be passed to subcommands."
        completer = ::remainingArgsCompleter
        validator = ::remainingArgsValidator
        docGenerator = ::remainingArgsDocGenerator
    }

    private fun subcommandCompleter(context: TabCompletionContext<SubcommandArguments>): List<String> {
        val command = context.arguments.rawArguments.first()
        val definition = context.commandDefinition

        return definition.subcommandNames.searchOrGetAll(command)
    }

    private fun subcommandValidator(result: ParseResult<String, SubcommandArguments>): Boolean {
        result.result ?: return false

        return validator.matches(result.result)
    }

    private fun subcommandDocGenerator(args: SubcommandArguments): String {
        return args.subcommand
            ?: children.keys.joinToString(
                separator = " | ",
                prefix = "[",
                postfix = "]"
            )
    }

    private fun remainingArgsCompleter(context: TabCompletionContext<SubcommandArguments>): List<String> {
        val args = context.arguments
        val subcommandName = args.subcommand!!
        val subcommand = children[subcommandName]
        val rawArguments = args.rawArguments
        val remainderStartIndex = args.lastSuccessfulResult()?.context?.currentIndex ?: return listOf()
        val remainingRawArgs = rawArguments.subList(remainderStartIndex, rawArguments.size).toTypedArray()

        return if (remainingRawArgs.isEmpty()) listOf()
        else subcommand?.onTabComplete(
            sender = context.sender,
            command = context.command,
            alias = context.alias,
            args = remainingRawArgs
        ) ?: listOf()
    }

    private fun remainingArgsValidator(parseResult: ParseResult<List<String>, SubcommandArguments>): Boolean {
        val result = parseResult.result ?: return false

        val subcommandName = parseResult.context.arguments.subcommand
        val subcommand = children[subcommandName]

        return subcommand?.definition?.executors?.any {
            it.arguments(result.toTypedArray()).valid()
        } ?: false
    }

    private fun remainingArgsDocGenerator(args: SubcommandArguments): String {
        val subcommandName = args.subcommand
        val subcommand = children[subcommandName]

        return if (subcommand == null) ""
        else {
            val executor = subcommand.definition.executors.firstOrNull()
            val remainderStartIndex = args.lastSuccessfulResult()?.context?.currentIndex ?: return ""
            val remainingRawArgs = rawArguments.subList(remainderStartIndex, rawArguments.size).toTypedArray()

            executor?.arguments(remainingRawArgs)?.documentation() ?: ""
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
