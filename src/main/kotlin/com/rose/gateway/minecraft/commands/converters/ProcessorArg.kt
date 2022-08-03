package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

fun <T, A : RunnerArguments<A>> RunnerArguments<A>.processor(
    body: ProcessorArgBuilder<T, A>.() -> Unit
): ProcessorArg<T, A> =
    genericParser(::ProcessorArgBuilder, body)

class ProcessorArg<T, A : RunnerArguments<A>>(val builder: ProcessorArgBuilder<T, A>) :
    RunnerArg<T, A, ProcessorArg<T, A>>(
        builder,
        completesAfterSatisfied = builder.completeAfterSatisfied
    ) {
    override fun typeName(): String = "CustomProcessor"

    override fun parseValue(context: ParseContext<A>): ParseResult<T, A> {
        return builder.processor(context)
    }
}

class ProcessorArgBuilder<T, A : RunnerArguments<A>> : ArgBuilder<T, A, ProcessorArg<T, A>>() {
    lateinit var processor: (ParseContext<A>) -> ParseResult<T, A>
    var completeAfterSatisfied = false

    override fun checkValidity() {
        if (!::processor.isInitialized) error("no processor given to processor argument")
    }

    override fun build(): ProcessorArg<T, A> {
        return ProcessorArg(this)
    }
}
