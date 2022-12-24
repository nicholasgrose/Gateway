package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import kotlin.reflect.KProperty

/**
 * A parser for a single command argument
 *
 * @param T The type of the result from parsing
 * @param A The type of the [CommandArgs] this parser parses for
 * @param P The type of this parser's child class
 * @property builder A [ParserBuilder] for this arg parser
 * @constructor Create an arg parser
 */
abstract class ArgParser<
    T,
    A : CommandArgs<A>,
    P : ArgParser<T, A, P>
    >(
    private val builder: ParserBuilder<T, A, P>
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
    fun completions(context: TabCompleteContext<A>): List<String> {
        return when {
            builder.completesAfterSatisfied -> builder.completer(self(), context)
            context.args.wasSuccessful(this) -> listOf()
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
    abstract fun parseValue(context: ParseContext<A>): ParseResult<T, A>

    /**
     * Parses a value from the given context and then runs it against the validation function
     *
     * @param context The context to parse the value from
     * @return The validated [ParseResult]
     */
    fun parseValidValue(context: ParseContext<A>): ParseResult<T, A> {
        val parseResult = parseValue(context)

        return if (parseResult is ParseResult.Success && builder.validator(self(), parseResult)) {
            ParseResult.Success(parseResult.result, parseResult.context)
        } else ParseResult.Failure(parseResult.context)
    }

    /**
     * A delegate that retrieves this parser's value from a set of command args
     *
     * @param thisRef The command args to pull this parser's value from
     * @param property The property that this delegate is store in
     * @return The value that this parser has in the args or null if none was found
     */
    operator fun getValue(thisRef: CommandArgs<A>, property: KProperty<*>): T {
        val parseResult = thisRef.parserResults[this]

        @Suppress("UNCHECKED_CAST")
        return when (parseResult) {
            is ParseResult.Success -> parseResult.result as T
            else -> error("Attempted to use command argument that failed parsing")
        }
    }
}
