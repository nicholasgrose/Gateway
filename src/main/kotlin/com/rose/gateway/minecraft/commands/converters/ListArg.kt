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

fun <T : Any, A : RunnerArguments<A>, R : RunnerArg<T, A, R>> listArg(
    body: ListArgBuilder<T, A, R>.() -> Unit
): ListArg<T, A, R> =
    genericArgBuilder(::ListArgBuilder, body)

class ListArg<T : Any, A : RunnerArguments<A>, R : RunnerArg<T, A, R>>(val builder: ListArgBuilder<T, A, R>) :
    RunnerArg<List<T>, A, ListArg<T, A, R>>(builder) {
    override fun typeName(): String = List::class.simpleName.toString()

    override fun parseValue(context: ParseContext<A>): ParseResult<List<T>, A> {
        val parser = builder.argType
        val results = mutableListOf<ParseResult<T, A>>()

        @Suppress("UNCHECKED_CAST")
        var currentResult = parser.parseValidValue(context)

        while (currentResult.succeeded) {
            results.add(currentResult)

            @Suppress("UNCHECKED_CAST")
            currentResult = parser.parseValidValue(currentResult.context)
        }

        return ParseResult(
            succeeded = true,
            result = results.map { it.result!! },
            context = results.lastOrNull()?.context ?: context
        )
    }
}

class ListArgBuilder<T : Any, A : RunnerArguments<A>, R : RunnerArg<T, A, R>> :
    ArgBuilder<List<T>, A, ListArg<T, A, R>>() {
    lateinit var argType: RunnerArg<T, A, R>

    override fun checkValidity() {
        if (!::argType.isInitialized) error("no type given for list argument")
    }

    override fun build(): ListArg<T, A, R> {
        return ListArg(this)
    }
}
