package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import org.bukkit.command.CommandSender

/**
 * An executor for a command
 *
 * @param A The type of the args that will be used for execution
 * @property executor The action that this executor defines
 * @property arguments A constructor for the arguments used by this executor
 * @constructor Create a command executor
 */
data class CommandExecutor<A : CommandArgs<A>>(
    val executor: ((CommandContext<A>) -> Boolean),
    val arguments: () -> A
) {
    /**
     * Tries to execute this executor
     *
     * @param definition The command definition to execute for
     * @param sender The sender of this execution from Bukkit
     * @param command The command for this execution from Bukkit
     * @param label The label for this execution from Bukkit
     * @param rawArguments The raw arguments passed into this execution
     * @return Whether the executor succeeded or null if it never executed
     */
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

    /**
     * Constructs filled-in arguments for the given raw arguments
     *
     * @param rawArguments The arguments that will be parsed
     * @return The constructed arguments
     */
    fun arguments(rawArguments: Array<String>): A {
        val args = arguments()

        args.forArguments(rawArguments.toList())

        return args
    }

    /**
     * Determines what completions exist for this executor
     *
     * @param sender The sender of this completion request
     * @param command The command being completed from Bukkit
     * @param alias The alias for the command being completed from Bukkit
     * @param commandDefinition The definition of the command being completed
     * @param rawArguments The raw arguments that already exist
     * @return A list of all possible completions
     */
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
                args = args,
                definition = commandDefinition
            )
        )
    }
}
