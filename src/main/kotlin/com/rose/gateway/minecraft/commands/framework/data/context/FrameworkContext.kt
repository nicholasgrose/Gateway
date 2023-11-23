package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs

/**
 * The context of an action for a command
 *
 * @param A The type of the args in the context
 * @property command The command in use
 * @property args The args in use
 * @constructor Create a framework context
 */
public sealed class FrameworkContext<A : CommandArgs<A>>(
    val command: Command,
    val args: A,
) {
    /**
     * The context of an action for a Bukkit command
     *
     * @param B The type of the Bukkit command context
     * @param A The type of the args in the context
     * @property bukkit The Bukkit context
     * @constructor Create a framework context for a Bukkit command
     *
     * @param command The command in use
     * @param args The args in use
     */
    public sealed class BukkitCommand<B : BukkitContext, A : CommandArgs<A>>(
        val bukkit: B,
        command: Command,
        args: A,
    ) : FrameworkContext<A>(command, args) {
        /**
         * The context for a command execution
         *
         * @param A The type of the args in the context
         * @constructor Create a command execution context
         *
         * @param bukkit The bukkit command execution context
         * @param command The command in use
         * @param args The args in use
         */
        public class CommandExecute<A : CommandArgs<A>>(
            bukkit: BukkitContext.CommandExecute,
            command: Command,
            args: A,
        ) : BukkitCommand<BukkitContext.CommandExecute, A>(bukkit, command, args)

        /**
         * The context for a command tab completion
         *
         * @param A The type of the args in the context
         * @property completingParser The parser that is currently doing completions
         * @constructor Create a command execution context
         *
         * @param bukkit The bukkit command execution context
         * @param command The command in use
         * @param args The args in use
         */
        public class TabComplete<A : CommandArgs<A>>(
            bukkit: BukkitContext.TabComplete,
            command: Command,
            args: A,
            val completingParser: ArgParser<*, A, *>,
        ) : BukkitCommand<BukkitContext.TabComplete, A>(bukkit, command, args)
    }
}

/**
 * @see FrameworkContext.BukkitCommand.CommandExecute
 */
public typealias CommandExecuteContext<A> = FrameworkContext.BukkitCommand.CommandExecute<A>

/**
 * @see FrameworkContext.BukkitCommand.TabComplete
 */
public typealias TabCompleteContext<A> = FrameworkContext.BukkitCommand.TabComplete<A>
