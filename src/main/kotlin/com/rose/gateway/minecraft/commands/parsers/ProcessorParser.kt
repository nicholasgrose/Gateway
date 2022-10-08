package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

fun <T, A : CommandArgs<A>> CommandArgs<A>.processor(
    body: ProcessorParserBuilder<T, A>.() -> Unit
): ProcessorParser<T, A> =
    genericParser(::ProcessorParserBuilder, body)

class ProcessorParser<T, A : CommandArgs<A>>(val builder: ProcessorParserBuilder<T, A>) :
    ArgParser<T, A, ProcessorParser<T, A>>(
        builder,
        completesAfterSatisfied = builder.completeAfterSatisfied
    ) {
    override fun typeName(): String = "CustomProcessor"

    override fun parseValue(context: ParseContext<A>): ParseResult<T, A> {
        return builder.processor(context)
    }
}

class ProcessorParserBuilder<T, A : CommandArgs<A>> : ParserBuilder<T, A, ProcessorParser<T, A>>() {
    lateinit var processor: (ParseContext<A>) -> ParseResult<T, A>
    var completeAfterSatisfied = false

    override fun checkValidity() {
        if (!::processor.isInitialized) error("no processor given to processor argument")
    }

    override fun build(): ProcessorParser<T, A> {
        return ProcessorParser(this)
    }
}
