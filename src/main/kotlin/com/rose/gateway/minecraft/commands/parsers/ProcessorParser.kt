package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext

/**
 * Parser for a custom-processed argument
 *
 * @param T The type of the processor's result
 * @param A The type of the args this parser is used by
 * @constructor Creates a processor parser
 *
 * @param builder The builder that defines this parser
 */
class ProcessorParser<T>(builder: ProcessorParserBuilder<T>) :
    ArgParser<T, ProcessorParser<T>, ProcessorParserBuilder<T>>(builder) {
    override fun typeName(): String = "CustomProcessor"

    override fun parseValue(context: ParseContext): ParseResult<T> {
        return builder.processor(context)
    }
}

/**
 * Builder for a [ProcessorParser]
 *
 * @param T The type of the processor's result
 * @param A The args the parser will be a part of
 * @constructor Creates a processor parser builder
 */
class ProcessorParserBuilder<T> :
    ParserBuilder<T, ProcessorParser<T>, ProcessorParserBuilder<T>>("Processor", "Some custom operation") {
    init {
        completesAfterSatisfied = false
    }

    /**
     * The function to use when parsing this argument
     */
    lateinit var processor: (ParseContext) -> ParseResult<T>

    override fun build(): ProcessorParser<T> {
        return ProcessorParser(this)
    }
}
