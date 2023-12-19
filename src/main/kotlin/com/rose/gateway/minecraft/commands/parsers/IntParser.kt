package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext

/**
 * Parser for an int argument
 *
 * @param A The type of the args this parser is used by
 * @constructor Creates an int parser
 *
 * @param builder The builder that defines this parser
 */
class IntParser(builder: IntParserBuilder) : ArgParser<Int, IntParser, IntParserBuilder>(builder) {
    override fun typeName(): String = "Int"

    private val internalParser = stringParser {
        name = builder.name
        description = builder.description
    }

    override fun parseValue(context: ParseContext): ParseResult<Int> {
        val stringResult = internalParser.parseValidValue(context)

        return if (stringResult is ParseResult.Success) {
            val result = stringResult.value.toIntOrNull()

            if (result != null) {
                ParseResult.Success(result, stringResult.context)
            } else {
                ParseResult.Failure(stringResult.context)
            }
        } else {
            ParseResult.Failure(stringResult.context)
        }
    }
}

/**
 * Builder for an [IntParser]
 *
 * @param A The args the parser will be a part of
 * @constructor Creates an int parser builder
 */
class IntParserBuilder : ParserBuilder<Int, IntParser, IntParserBuilder>("Integer", "A whole number") {
    override fun build(): IntParser {
        return IntParser(this)
    }
}
