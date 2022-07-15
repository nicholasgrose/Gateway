package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import kotlin.reflect.KClass

fun <T : Any, A : RunnerArguments<A>> listArg(body: ListArgBuilder<T, A>.() -> Unit): ListArg<T, A> =
    genericArgBuilder(::ListArgBuilder, body)

fun <T : Any, A : RunnerArguments<A>> RunnerArguments<A>.list(body: ListArgBuilder<T, A>.() -> Unit): ListArg<T, A> =
    genericParser(::ListArgBuilder, body)

class ListArg<T : Any, A : RunnerArguments<A>>(val builder: ListArgBuilder<T, A>) :
    RunnerArg<List<T>, A, ListArg<T, A>>(builder) {
    override fun typeName(): String = List::class.simpleName.toString()

    override fun parseValue(context: ParseContext<A>): ParseResult<List<T>, A> {
        val parser = parserMap()[builder.type] ?: return failedParseResult(context)
        val results = mutableListOf<ParseResult<T, A>>()

        @Suppress("UNCHECKED_CAST")
        var currentResult = parser.parseValue(context) as ParseResult<T, A>

        while (currentResult.succeeded) {
            results.add(currentResult)

            @Suppress("UNCHECKED_CAST")
            currentResult = parser.parseValue(context) as ParseResult<T, A>
        }

        return if (results.isEmpty()) failedParseResult(context)
        else ParseResult(
            succeeded = true,
            result = results.map { it.result!! },
            context = results.last().context
        )
    }

    private fun parserMap(): Map<KClass<out Any>, RunnerArg<out Any, A, *>> = mapOf(
        Boolean::class to booleanArg {
            name = builder.name
            description = builder.description
        },
        Integer::class to intArg {
            name = builder.name
            description = builder.description
        },
        String::class to stringArg {
            name = builder.name
            description = builder.description
        }
    )
}

class ListArgBuilder<T : Any, A : RunnerArguments<A>> : ArgBuilder<List<T>, A, ListArg<T, A>>() {
    lateinit var type: KClass<T>

    override fun checkValidity() {
        if (!::type.isInitialized) error("no type given for list argument")
    }

    override fun build(): ListArg<T, A> {
        return ListArg(this)
    }
}
