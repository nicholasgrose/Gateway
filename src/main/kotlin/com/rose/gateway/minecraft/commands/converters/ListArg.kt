package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

fun <T : Any, A : RunnerArguments<A>, R : RunnerArg<T, A, R>> RunnerArguments<A>.list(
    body: ListArgBuilder<T, A, R>.() -> Unit
): ListArg<T, A, R> =
    genericParser(::ListArgBuilder, body)

class ListArg<T : Any, A : RunnerArguments<A>, R : RunnerArg<T, A, R>>(val builder: ListArgBuilder<T, A, R>) :
    RunnerArg<List<T>, A, ListArg<T, A, R>>(
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

class ListArgBuilder<T : Any, A : RunnerArguments<A>, R : RunnerArg<T, A, R>> :
    ArgBuilder<List<T>, A, ListArg<T, A, R>>() {
    init {
        completer = {
            element.completions(it)
        }
    }

    lateinit var element: RunnerArg<T, A, R>
    var requireNonEmpty = true

    override fun checkValidity() {
        if (!::element.isInitialized) error("no type given for list argument")
    }

    override fun build(): ListArg<T, A, R> {
        return ListArg(this)
    }
}
