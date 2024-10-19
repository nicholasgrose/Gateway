package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.data.parser.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

/**
 * Adds a custom-processed argument to these args
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The arg body
 * @receiver The builder for the parser
 * @return The built parser
 */
@Suppress("MaxLineLength")
fun <T, A> CommandArgs<A>.processor(body: ProcessorParserBuilder<T, A>.() -> Unit): ProcessorParser<T, A> where A : CommandArgs<A> =
    genericParser(::ProcessorParserBuilder, body)

/**
 * Parser for a custom-processed argument
 *
 * @param T The type of the processor's result
 * @param A The type of the args this parser is used by
 * @constructor Creates a processor parser
 *
 * @param builder The builder that defines this parser
 */
@Suppress("MaxLineLength")
class ProcessorParser<T, A>(
    override val builder: ProcessorParserBuilder<T, A>,
) : ArgParser<T, A, ProcessorParser<T, A>>(builder) where A : CommandArgs<A> {
    override fun typeName(): String = "CustomProcessor"

    override fun parseValue(context: ParseContext<A>): ParseResult<T, A> = builder.processor(context)
}

/**
 * Builder for a [ProcessorParser]
 *
 * @param T The type of the processor's result
 * @param A The args the parser will be a part of
 * @constructor Creates a processor parser builder
 */
class ProcessorParserBuilder<T, A : CommandArgs<A>> : ParserBuilder<T, A, ProcessorParser<T, A>>() {
    init {
        completesAfterSatisfied = false
    }

    /**
     * The function to use when parsing this argument
     */
    lateinit var processor: (ParseContext<A>) -> ParseResult<T, A>

    override fun checkValidity() {
        if (!::processor.isInitialized) error("no processor given to processor argument")
    }

    override fun build(): ProcessorParser<T, A> = ProcessorParser(this)
}
