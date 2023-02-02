package com.rose.gateway.minecraft.commands.framework.nesting

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.emptyUsageGenerator
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext
import com.rose.gateway.minecraft.commands.framework.data.context.ParserSpecificContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference
import com.rose.gateway.minecraft.commands.framework.data.executor.ExecutorArgsPair
import com.rose.gateway.minecraft.commands.parsers.UnitParser
import com.rose.gateway.minecraft.commands.parsers.list
import com.rose.gateway.minecraft.commands.parsers.processor
import com.rose.gateway.minecraft.commands.parsers.stringParser

/**
 * Args for a referenced set of command args
 *
 * @param A The type of the args referenced within the reference args
 * @constructor Create empty reference args
 */
class ReferenceArgs<A : CommandArgs<A>>(
    private val argsRef: ArgsReference<A>,
    private val command: Command
) : CommandArgs<ReferenceArgs<A>>() {
    companion object {
        /**
         * Creates a reference args constructor that references some args paired with some command
         *
         * @param A The type of the args referenced
         * @param argsRef The reference to the args
         * @param command The command to be used
         * @return The reference args constructor
         */
        fun <A : CommandArgs<A>> forArgs(argsRef: ArgsReference<A>, command: Command): () -> ReferenceArgs<A> {
            return {
                ReferenceArgs(argsRef, command)
            }
        }
    }

    /**
     * The result of parsing with the contained args
     */
    val containedArgs by processor<A, ReferenceArgs<A>> {
        name = "Inner Args"
        description = "The inner args after attempting parsing"

        processor = { context ->
            val args = argsRef.args()
            val result = args.parseArguments(argsRef, context)
            val parseContext = ParseContext(
                context.command,
                context.args,
                context.bukkit,
                context.parserContext,
                result.lastIndex()
            )

            ParseResult.Success(result, parseContext)
        }
    }

    /**
     * Represents the validity of the contained args
     */
    val containedArgsValidity by processor<Unit, ReferenceArgs<A>> {
        name = "Inner Args Validity"
        description = "Whether the inner args successfully finished parsing"

        processor = {
            if (containedArgs.valid(true)) {
                ParseResult.Success(Unit, it)
            } else {
                ParseResult.Failure(it)
            }
        }

        usageGenerator = {
            containedArgs.usages()
        }

        completer = { context ->
            containedArgs.completions(
                argsRef,
                TabCompleteContext(
                    context.command,
                    context.args,
                    context.bukkit,
                    ParserSpecificContext(
                        argsRef,
                        UnitParser()
                    )
                )
            )
        }
    }

    /**
     * The args remaining for the command to use
     */
    val remainingArgs by list {
        element = stringParser {
            name = "Remaining Arg"
            description = "One of the args remaining"
        }
        name = "Remaining Args"
        description = "All of the args to be passed to the internal command"
        usageGenerator = emptyUsageGenerator()
        requireNonEmpty = false
    }

    /**
     * The executors of the command ranked by relevance
     *
     * @see Command.mostSuccessfulExecutors
     */
    val rankedExecutors by processor<List<ExecutorArgsPair<*>>, ReferenceArgs<A>> {
        name = "Ranked Executors"
        description = "A list of command executors paired with their parsed arguments and ranked by relevance"
        processor = { context ->
            val mostSuccessfulExecutors = command.mostSuccessfulExecutors(context)
            ParseResult.Success(mostSuccessfulExecutors, context)
        }
        usageGenerator = emptyUsageGenerator()
        completesAfterSatisfied = true
    }

    /**
     * Value that is only available if any of the executors succeeded
     */
    val executorsValidity by processor<Unit, ReferenceArgs<A>> {
        name = "Executors Validation"
        description = "Whether the ranked executors are valid"

        processor = { context ->
            val succeeded = rankedExecutors.any { it.args.valid() }

            if (succeeded) ParseResult.Success(Unit, context)
            else ParseResult.Failure(context)
        }

        usageGenerator = {
            rankedExecutors.flatMap {
                it.args.usages()
            }
        }

        completer = { context ->
            context.args.parsed[argsRef] = containedArgs

            if (remainingArgs.isEmpty()) listOf()
            else command.complete(
                TabCompleteContext(
                    context.command,
                    context.args,
                    context.bukkit,
                    ParserSpecificContext(
                        argsRef,
                        UnitParser()
                    )
                ),
                rankedExecutors
            )
        }
    }
}
