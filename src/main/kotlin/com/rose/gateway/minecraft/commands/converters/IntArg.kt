package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

fun <A : RunnerArguments<A>> intArg(body: IntArgBuilder<A>.() -> Unit): IntArg<A> =
    genericArgBuilder(::IntArgBuilder, body)

fun <A : RunnerArguments<A>> RunnerArguments<A>.int(body: IntArgBuilder<A>.() -> Unit): IntArg<A> =
    genericParser(::IntArgBuilder, body)

class IntArg<A : RunnerArguments<A>>(builder: IntArgBuilder<A>) :
    RunnerArg<Int, A, IntArg<A>>(builder) {
    override fun typeName(): String = Int::class.simpleName.toString()

    private val internalParser = stringArg<A> {
        name = builder.name
        description = builder.description
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<Int, A> {
        val stringResult = internalParser.parseValidValue(context)
        val result = stringResult.result?.toInt()

        return ParseResult(
            succeeded = result != null,
            context = stringResult.context,
            result = result
        )
    }
}

class IntArgBuilder<A : RunnerArguments<A>> : ArgBuilder<Int, A, IntArg<A>>() {
    override fun checkValidity() = Unit

    override fun build(): IntArg<A> {
        return IntArg(this)
    }
}
