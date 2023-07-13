package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.emptyArgs
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.emptyUsageGenerator
import com.rose.gateway.minecraft.commands.parsers.UnitParser
import com.rose.gateway.minecraft.commands.parsers.list
import com.rose.gateway.minecraft.commands.parsers.processor
import com.rose.gateway.minecraft.commands.parsers.string
import com.rose.gateway.minecraft.commands.parsers.stringParser

/**
 * The arguments for a subcommand
 *
 * @property command The command this subcommand executes
 * @constructor Create empty subcommand args for a command
 */
class SubcommandArgs(val command: Command) : CommandArgs<SubcommandArgs>() {
    companion object {
        /**
         * Gives a constructor for [SubcommandArgs] that use a specific command
         *
         * @param command The command the subcommand executes
         * @return A constructor a the command's [SubcommandArgs]
         */
        fun forCommand(command: Command): () -> SubcommandArgs {
            return {
                SubcommandArgs(command)
            }
        }
    }

    private val subcommandParser = string {
        name = "subcommand"
        description = "The subcommand to run."
        completer = { listOf(command.definition.name) }
        validator = { it.result == command.definition.name }
        usageGenerator = { listOf(command.definition.name) }
    }

    /**
     * The args remaining for the command to use
     */
    val remainingArgs by list {
        element = stringParser {
            name = "Remaining Arg"
            description = "One of the args remaining."
        }
        name = "Remaining Args"
        description = "All of the args to be passed to subcommands."
        usageGenerator = emptyUsageGenerator()
        requireNonEmpty = false
    }

    /**
     * The executors of the command ranked by relevance
     *
     * @see Command.mostSuccessfulExecutors
     */
    val rankedExecutors by processor {
        name = "Ranked Executors"
        description = "A list of command executors paired with their parsed arguments and ranked by relevance"
        processor = { context ->
            val mostSuccessfulExecutors = command.mostSuccessfulExecutors(remainingArgs)
            ParseResult.Success(mostSuccessfulExecutors, context)
        }
        usageGenerator = emptyUsageGenerator()
        completesAfterSatisfied = true
    }

    /**
     * Value that is only available if any of the executors succeeded
     */
    @Suppress("unused", "RemoveExplicitTypeArguments")
    val executorsValid by processor<Unit, SubcommandArgs> {
        name = "Executors Validation"
        description = "Whether the ranked executors are valid."

        processor = { context ->
            val succeeded = rankedExecutors.any { it.args.valid() }

            if (succeeded) {
                ParseResult.Success(Unit, context)
            } else {
                ParseResult.Failure(context)
            }
        }

        usageGenerator = {
            val subcommandDocs = if (wasSuccessful(subcommandParser)) {
                rankedExecutors.flatMap {
                    it.args.usages()
                }
            } else {
                listOf()
            }

            subcommandDocs
        }

        completer = { context ->
            if (remainingArgs.isEmpty()) {
                listOf()
            } else {
                command.complete(
                    TabCompleteContext(
                        bukkit = context.bukkit,
                        command = command,
                        args = emptyArgs(remainingArgs),
                        completingParser = UnitParser(),
                    ),
                    rankedExecutors,
                )
            }
        }
    }
}
