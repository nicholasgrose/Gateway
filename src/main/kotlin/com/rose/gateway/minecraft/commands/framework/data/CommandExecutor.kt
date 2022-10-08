package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import org.bukkit.command.CommandSender

data class CommandExecutor<A : CommandArgs<A>>(
    val executor: ((CommandContext<A>) -> Boolean),
    val arguments: () -> A
) {
    fun tryExecute(
        definition: CommandDefinition,
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        rawArguments: Array<String>
    ): Boolean? {
        val args = arguments(rawArguments)

        return if (args.valid()) {
            executor(
                CommandContext(
                    definition = definition,
                    sender = sender,
                    command = command,
                    label = label,
                    args = args
                )
            )
        } else null
    }

    fun arguments(rawArguments: Array<String>): A {
        val args = arguments()

        args.forArguments(rawArguments.toList())

        return args
    }

    fun completions(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        alias: String,
        commandDefinition: CommandDefinition,
        rawArguments: Array<String>
    ): List<String> {
        val args = arguments(rawArguments)

        return args.completions(
            TabCompletionContext(
                sender = sender,
                command = command,
                alias = alias,
                arguments = args,
                commandDefinition = commandDefinition
            )
        )
    }
}
