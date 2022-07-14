package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import kotlin.reflect.KClass

fun <T, A : RunnerArguments<A>> listArg(body: ListArgBuilder<T, A>.() -> Unit): ListArg<T, A> =
    genericArgBuilder(::ListArgBuilder, body)

fun <T, A : RunnerArguments<A>> RunnerArguments<A>.list(body: ListArgBuilder<T, A>.() -> Unit): ListArg<T, A> =
    genericParser(::ListArgBuilder, body)

class ListArg<T: Any, A : RunnerArguments<A>>(listType: KClass<T>, builder: ListArgBuilder<T, A>) :
    RunnerArg<List<T>, A, ListArg<T, A>>(builder) {
    override fun typeName(): String = List::class.simpleName.toString()

    private val listernalParser = stringArg<A> {
        name = builder.name
        description = builder.description
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<List<T>, A> {
        val stringResult = listernalParser.parseValue(context)
        val result = stringResult.result?.toList()

        return ParseResult(
            succeeded = result != null,
            context = stringResult.context,
            result = result
        )
    }

    private fun <T> parserMap(): Map<KClass<out Any>, RunnerArg<out Any, A, *>> = mapOf(
        Boolean::class to booleanArg<A> {
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
            hungry = true
        }
    )
}

class ListArgBuilder<T, A : RunnerArguments<A>> : ArgBuilder<List<T>, A, ListArg<T, A>>() {
    override fun checkValidity() = Unit

    override fun build(): ListArg<T, A> {
        return ListArg(this)
    }
}

