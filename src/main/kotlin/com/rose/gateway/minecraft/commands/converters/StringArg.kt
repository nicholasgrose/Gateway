package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

fun <A : RunnerArguments<A>> RunnerArguments<A>.string(body: StringArgBuilder<A>.() -> Unit): StringArg<A> =
    genericParser(::StringArgBuilder, body)

class StringArg<A : RunnerArguments<A>>(builder: StringArgBuilder<A>) : RunnerArg<String, A, StringArg<A>>(builder) {

    override fun typeName(): String = String::class.simpleName.toString()

    override fun parseValue(context: ParseContext<A>): ParseResult<String, A> {
        val args = context.arguments
        val currentIndex = context.currentIndex
        val result = args.rawArguments.getOrNull(currentIndex)

        return ParseResult(
            succeeded = result != null,
            context = ParseContext(
                arguments = args,
                currentIndex = currentIndex + 1
            ),
            result = result
        )
    }
}

class StringArgBuilder<A : RunnerArguments<A>> : ArgBuilder<String, A, StringArg<A>>() {
    override fun checkValidity() = Unit

    override fun build(): StringArg<A> {
        return StringArg(this)
    }
}
