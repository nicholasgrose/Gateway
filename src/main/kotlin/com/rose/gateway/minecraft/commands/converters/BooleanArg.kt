package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

fun <A : RunnerArguments<A>> RunnerArguments<A>.boolean(body: BooleanArgBuilder<A>.() -> Unit): BooleanArg<A> =
    genericParser(::BooleanArgBuilder, body)

class BooleanArg<A : RunnerArguments<A>>(builder: BooleanArgBuilder<A>) :
    RunnerArg<Boolean, A, BooleanArg<A>>(builder) {
    override fun typeName(): String = "Boolean"

    private val internalParser = stringArg<A> {
        name = builder.name
        description = builder.description
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<Boolean, A> {
        val stringResult = internalParser.parseValidValue(context)
        val result = stringResult.result?.toBooleanStrictOrNull()

        return ParseResult(
            succeeded = result != null,
            context = stringResult.context,
            result = result
        )
    }
}

class BooleanArgBuilder<A : RunnerArguments<A>> : ArgBuilder<Boolean, A, BooleanArg<A>>() {
    init {
        this.completer = { listOf("true", "false") }
    }

    override fun checkValidity() = Unit

    override fun build(): BooleanArg<A> {
        return BooleanArg(this)
    }
}
