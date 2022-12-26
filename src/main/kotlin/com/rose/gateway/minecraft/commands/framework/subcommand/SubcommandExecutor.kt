package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor
import com.rose.gateway.minecraft.commands.framework.emptyArgs

/**
 * Builds a subcommand executor for a command
 *
 * @param command The command to wrap into a subcommand
 * @return The executor for the subcommand
 */
fun subcommandExecutor(command: Command): CommandExecutor<SubcommandArgs> = CommandExecutor(
    ::subcommandRunner,
    SubcommandArgs.forCommand(command)
)

/**
 * The runner for subcommands
 *
 * @param context The command context in which this runner executes
 * @return Whether this runner successfully executed
 */
fun subcommandRunner(context: CommandExecuteContext<SubcommandArgs>): Boolean {
    val args = context.args
    val remainingArguments = args.remainingArgs
    val commandResult = context.args.command.execute(
        CommandExecuteContext(
            bukkit = context.bukkit,
            command = context.command,
            args = emptyArgs(remainingArguments)
        ),
        args.rankedExecutors
    )

    return commandResult.succeeded
}
