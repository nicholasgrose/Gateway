package com.rose.gateway.minecraft.commands.framework.args

import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext

/**
 * A parser for a single command argument
 *
 * @param T The type of the result from parsing
 * @param A The type of the [CommandArgs] this parser parses for
 * @param P The type of this parser's child class
 * @property builder A [ParserBuilder] for this arg parser
 * @constructor Create an arg parser
 */
abstract class ArgParser<T, P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B>>(
    open val builder: B,
) {
    /**
     * Gives the name of this parser
     *
     * @return This parser's name
     */
    fun name(): String = builder.name

    /**
     * Gives the type returned by this parser as a string
     *
     * @return This parser's result type
     */
    abstract fun typeName(): String

    /**
     * Gives the tab completions for this argument based on the given context
     *
     * @param context The context of this tab completion
     * @return A list of possible tab completions
     */
    fun completions(context: TabCompleteContext): List<String> {
        return when {
            builder.completesAfterSatisfied -> builder.completer(self(), context)
            context.resultOf(self()) is ParseResult.Success<T> -> listOf()
            else -> builder.completer(self(), context)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun self(): P = this as P

    /**
     * Generates usages for this argument
     *
     * @return All possible usages for this argument
     */
    fun generateUsages(): List<String> = builder.usageGenerator(self())

    /**
     * Parses the value given the existing context
     *
     * @param context The context to parse the value from
     * @return The result of parsing
     */
    abstract fun parseValue(context: ParseContext): ParseResult<T>

    /**
     * Parses a value from the given context and then runs it against the validation function
     *
     * @param context The context to parse the value from
     * @return The validated [ParseResult]
     */
    fun parseValidValue(context: ParseContext): ParseResult<T> {
        val existingParseResult = context.resultOfOrDefault(self())

        val parseResult = when {
            builder.completesAfterSatisfied -> parseValue(context)
            existingParseResult is ParseResult.Failure -> parseValue(context)
            else -> existingParseResult
        }

        return if (parseResult is ParseResult.Success && builder.validator(self(), parseResult)) {
            ParseResult.Success(parseResult.value, parseResult.context)
        } else {
            ParseResult.Failure(parseResult.context)
        }
    }
}