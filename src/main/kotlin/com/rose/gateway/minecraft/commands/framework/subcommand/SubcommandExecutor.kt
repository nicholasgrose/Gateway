package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor

fun subcommandExecutor(children: Map<String, Command>): CommandExecutor<SubcommandArgs> =
    CommandExecutor(
        ::subcommandRunner,
        SubcommandArgs.forChildCommands(children)
    )

fun subcommandRunner(context: CommandContext<SubcommandArgs>): Boolean {
    val args = context.args
    val childCommand = args.subcommand
    val remainingArguments = args.remainingArgs?.toTypedArray()

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
