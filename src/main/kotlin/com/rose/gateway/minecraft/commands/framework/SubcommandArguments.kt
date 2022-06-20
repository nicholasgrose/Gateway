package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import com.rose.gateway.minecraft.commands.framework.runner.string

class SubcommandArguments(private val subcommands: List<String>) : RunnerArguments<SubcommandArguments>(
    allowUnusedArguments = true
) {
    companion object {
        fun forSubcommands(subcommands: List<String>): () -> SubcommandArguments {
            return {
                SubcommandArguments(subcommands)
            }
        }
    }

    val subcommand by string {
        name = "subcommand"
        description = "The subcommand to run."
    }

    override fun documentation(): String = subcommands.joinToString(
        separator = " | ",
        prefix = "[",
        postfix = "]"
    )

    override fun completions(context: TabCompletionContext<SubcommandArguments>): List<String> {
        val rawArguments = context.arguments.rawArguments
        val definition = context.commandDefinition

        return if (rawArguments.size > 1) {
            val subcommand = definition.subcommands[context.arguments.subcommand]
            subcommand?.onTabComplete(
                context.sender,
                context.command,
                context.alias,
                rawArguments.subList(1, rawArguments.size).toTypedArray()
            ) ?: listOf()
        } else definition.subcommandNames.searchOrGetAll(rawArguments.first())
    }
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
