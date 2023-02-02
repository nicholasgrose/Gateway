package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext

/**
 * Adds a list argument to these args
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The arg body
 * @receiver The builder for the parser
 * @return The built parser
 */
fun <T : Any, A : CommandArgs<A>, R : ArgParser<T, A, R>> CommandArgs<A>.list(
    body: ListParserBuilder<T, A, R>.() -> Unit
): ListParser<T, A, R> = genericParser(::ListParserBuilder, body)

/**
 * Parser for a list argument
 *
 * @param T The type of the list elements
 * @param A The type of the args this parser is used by
 * @param P The type of the parser for list elements
 * @constructor Creates a list parser
 *
 * @param builder The builder that defines this parser
 */
class ListParser<T, A, P>(override val builder: ListParserBuilder<T, A, P>) :
    ArgParser<List<T>, A, ListParser<T, A, P>>(builder) where
T : Any, A : CommandArgs<A>, P : ArgParser<T, A, P> {
    private val parser = builder.element

    override fun typeName(): String = "List<${parser.typeName()}>"

    override fun parseValue(context: ParseContext<A>): ParseResult<List<T>, A> {
        val results = mutableListOf<ParseResult.Success<T, A>>()
        var currentResult = parser.parseValidValue(context)

        while (currentResult is ParseResult.Success) {
            results.add(currentResult)

            currentResult = parser.parseValidValue(currentResult.context)
        }

        return if (builder.requireNonEmpty && results.isEmpty()) ParseResult.Failure(context)
        else ParseResult.Success(
            results.map { it.result },
            results.lastOrNull()?.context ?: context
        )
    }
}

/**
 * Builder for a [ListParser]
 *
 * @param T The type of the list elements
 * @param A The args the parser will be a part of
 * @param P The type of the parser for list elements
 * @constructor Creates a list parser builder
 */
class ListParserBuilder<T : Any, A : CommandArgs<A>, P : ArgParser<T, A, P>> :
    ParserBuilder<List<T>, A, ListParser<T, A, P>>() {
    init {
        completer = {
            element.completions(it)
        }
        completesAfterSatisfied = true
    }

    /**
     * The parser to be used for each list element
     */
    lateinit var element: ArgParser<T, A, P>

    /**
     * Whether the parsed list is valid when empty
     */
    var requireNonEmpty = true

    override fun checkValidity() {
        if (!::element.isInitialized) error("no type given for list argument")
    }

    override fun build(): ListParser<T, A, P> {
        return ListParser(this)
    }
}
