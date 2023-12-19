package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext

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
class ListParser<T, P, B>(builder: ListParserBuilder<T, P, B>) :
    ArgParser<List<T>, ListParser<T, P, B>, ListParserBuilder<T, P, B>>(builder)
    where T : Any, P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
    private val parser = builder.element

    override fun typeName(): String = "List<${parser.typeName()}>"

    override fun parseValue(context: ParseContext): ParseResult<List<T>> {
        val results = mutableListOf<ParseResult.Success<T>>()
        var currentResult = parser.parseValidValue(context)

        while (currentResult is ParseResult.Success<T>) {
            results.add(currentResult)

            currentResult = parser.parseValidValue(currentResult.context)
        }

        return if (builder.requireNonEmpty && results.isEmpty()) {
            ParseResult.Failure(context)
        } else {
            ParseResult.Success(
                results.map { it.value },
                results.lastOrNull()?.context ?: context,
            )
        }
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
class ListParserBuilder<T, P, B> :
    ParserBuilder<List<T>, ListParser<T, P, B>, ListParserBuilder<T, P, B>>("List", "A list of values")
    where T : Any, P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
    init {
        completer = {
            element.completions(it)
        }
        completesAfterSatisfied = true
    }

    /**
     * The parser to be used for each list element
     */
    lateinit var element: ArgParser<T, P, B>

    /**
     * Whether the parsed list is valid when empty
     */
    var requireNonEmpty = true

    override fun build(): ListParser<T, P, B> {
        return ListParser(this)
    }
}
