package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference

/**
 * The context of an action for a command
 *
 * @param B The type of the bukkit context
 * @property command The command in use
 * @property args Data about the args in this context
 * @property bukkit The context of the bukkit command
 * @constructor Create a framework context
 */
public sealed class FrameworkContext<B : BukkitContext>(
    val command: Command,
    val args: ArgsContext,
    val bukkit: B
) {
    /**
     * The context of executing a command
     *
     * @constructor Create a command execute context
     *
     * @param command The command in use
     * @param args Data about the args in this context
     * @param bukkit The context of the bukkit command
     */
    class CommandExecute(
        command: Command,
        args: ArgsContext,
        bukkit: BukkitContext.CommandExecute
    ) : FrameworkContext<BukkitContext.CommandExecute>(command, args, bukkit)

    /**
     * The context around an action involving a specific command argument
     *
     * @param A The type of the args in the context
     * @param B The type of the bukkit context
     * @property parserContext The context of the parser current being used
     * @constructor Create a framework command arg context
     *
     * @param command The command in use
     * @param argsContext Data about the args in this context
     * @param bukkit The context of the bukkit command
     */
    public sealed class CommandArg<A : CommandArgs<A>, B : BukkitContext>(
        command: Command,
        argsContext: ArgsContext,
        bukkit: B,
        val parserContext: ParserSpecificContext<A>
    ) : FrameworkContext<B>(command, argsContext, bukkit) {
        /**
         * The context around parsing a command
         *
         * @param A The type of the args in the context
         * @property currentIndex The next index to use from the raw args
         * @constructor Create a parse command arg context
         *
         * @param command The command in use
         * @param args Data about the args in this context
         * @param parser The context of the parser current being used
         * @param bukkit The context of the bukkit command
         */
        class TabComplete<A : CommandArgs<A>>(
            command: Command,
            args: ArgsContext,
            bukkit: BukkitContext.TabComplete,
            parser: ParserSpecificContext<A>
        ) : CommandArg<A, BukkitContext.TabComplete>(command, args, bukkit, parser)

        /**
         * The context around parsing a command
         *
         * @param A The type of the args in the context
         * @property currentIndex The next index to use from the raw args
         * @constructor Create a parse command arg context
         *
         * @param command The command in use
         * @param args Data about the args in this context
         * @param parser The context of the parser current being used
         * @param bukkit The context of the bukkit command
         */
        class Parse<A : CommandArgs<A>>(
            command: Command,
            args: ArgsContext,
            bukkit: BukkitContext,
            parser: ParserSpecificContext<A>,
            val currentIndex: Int
        ) : CommandArg<A, BukkitContext>(command, args, bukkit, parser)
    }

    /**
     * Gives the arguments for an args reference and casts them to the correct type
     *
     * @param A The type of the args to get
     * @param argsRef The reference to the args
     * @return The referenced args
     */
    fun <A : CommandArgs<A>> argsFor(argsRef: ArgsReference<A>): A {
        val pulledArgs = args.parsed[argsRef]

        @Suppress("UNCHECKED_CAST") return pulledArgs as A
    }

    /**
     * Gets the ref for an args instance or creates one if it doesn't yet exist
     *
     * @param A The type of the command args the result references
     * @param argsConstructor The args to get the ref of
     * @receiver None
     * @return The reference for the args
     */
    fun <A : CommandArgs<A>> refFor(commandArgs: A): ArgsReference<A> {
        val pulledRef = args.parsed[commandArgs]

        @Suppress("UNCHECKED_CAST") return pulledRef as ArgsReference<A>
    }
}

/**
 * The context for command execution
 */
typealias CommandExecuteContext = FrameworkContext.CommandExecute

/**
 * The context for completing a command argument
 */
typealias TabCompleteContext<A> = FrameworkContext.CommandArg.TabComplete<A>

/**
 * The context for parsing a command argument
 */
typealias ParseContext<A> = FrameworkContext.CommandArg.Parse<A>
