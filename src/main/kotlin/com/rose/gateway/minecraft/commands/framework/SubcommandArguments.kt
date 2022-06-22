package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.converters.string
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

class SubcommandArguments(private val subcommands: List<String>) : RunnerArguments<SubcommandArguments>(
    unusedArgumentsAllowed = true
) {
    companion object {
        fun forSubcommands(subcommands: List<String>): () -> SubcommandArguments {
            return {
                SubcommandArguments(subcommands)
            }
        }
    }

    val subcommand: String? by string {
        name = "subcommand"
        description = "The subcommand to run."
        completer = { context ->
            val command = context.arguments.subcommand ?: ""
            val definition = context.commandDefinition

            if (hasUnusedArgs()) {
                val subcommand = definition.subcommands[command]

                subcommand?.onTabComplete(
                    sender = context.sender,
                    command = context.command,
                    alias = context.alias,
                    args = remainingArguments()
                ) ?: listOf()
            } else definition.subcommandNames.searchOrGetAll(command)
        }
    }

    override fun documentation(): String = subcommands.joinToString(
        separator = " | ",
        prefix = "[",
        postfix = "]"
    )
}

fun subcommandRunner(context: CommandContext<SubcommandArguments>): Boolean {
    val childCommand = context.definition.subcommands[context.arguments.subcommand]
    val remainingArguments = context.arguments.remainingArguments()

    return if (childCommand == null) false
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
