package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

fun <T : Any, A : CommandArgs<A>, R : ArgParser<T, A, R>> CommandArgs<A>.list(
    body: ListParserBuilder<T, A, R>.() -> Unit
): ListParser<T, A, R> =
    genericParser(::ListParserBuilder, body)

class ListParser<T : Any, A : CommandArgs<A>, R : ArgParser<T, A, R>>(val builder: ListParserBuilder<T, A, R>) :
    ArgParser<List<T>, A, ListParser<T, A, R>>(
        builder,
        completesAfterSatisfied = true
    ) {
    private val parser = builder.element

    override fun typeName(): String = "List<${parser.typeName()}>"

    override fun parseValue(context: ParseContext<A>): ParseResult<List<T>, A> {
        val results = mutableListOf<ParseResult<T, A>>()
        var currentResult = parser.parseValidValue(context)

        while (currentResult.succeeded) {
            results.add(currentResult)

            currentResult = parser.parseValidValue(currentResult.context)
        }

        return if (builder.requireNonEmpty && results.isEmpty()) failedParseResult(context)
        else ParseResult(
            succeeded = results.all { it.succeeded },
            result = results.map { it.result!! },
            context = results.lastOrNull()?.context ?: context
        )
    }
}

class ListParserBuilder<T : Any, A : CommandArgs<A>, R : ArgParser<T, A, R>> :
    ParserBuilder<List<T>, A, ListParser<T, A, R>>() {
    init {
        completer = {
            element.completions(it)
        }
    }

    lateinit var element: ArgParser<T, A, R>
    var requireNonEmpty = true

    override fun checkValidity() {
        if (!::element.isInitialized) error("no type given for list argument")
    }

    override fun build(): ListParser<T, A, R> {
        return ListParser(this)
    }
}
