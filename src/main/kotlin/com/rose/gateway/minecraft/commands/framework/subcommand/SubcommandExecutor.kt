package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor

/**
 * Creates a subcommand executor for the children of a command
 *
 * @param children The children to create a subcommand executor for
 * @return The created subcommand executor
 */
fun subcommandExecutor(children: Map<String, Command>): CommandExecutor<SubcommandArgs> =
    CommandExecutor(
        ::subcommandRunner,
        SubcommandArgs.forChildCommands(children)
    )

/**
 * The runner for subcommands
 *
 * @param context The command context in which this runner executes
 * @return Whether this runner successfully executed
 */
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
