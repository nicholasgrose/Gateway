package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.parsers.string

/**
 * The arguments for a subcommand
 *
 * @property name The name of this subcommand
 * @constructor Create empty subcommand args for a command
 */
class SubcommandArgs(val name: String) : CommandArgs<SubcommandArgs>() {
    companion object {
        /**
         * Gives a constructor for [SubcommandArgs] that use a specific command
         *
         * @param name The name of this subcommand
         * @return A constructor a the command's [SubcommandArgs]
         */
        fun forCommand(name: String): () -> SubcommandArgs {
            return {
                SubcommandArgs(name)
            }
        }
    }

    init {
        string {
            name = "subcommand"
            description = "The subcommand to run."
            completer = { listOf(name) }
            validator = { it.result == name }
            usageGenerator = { listOf(name) }
        }
    }
}
