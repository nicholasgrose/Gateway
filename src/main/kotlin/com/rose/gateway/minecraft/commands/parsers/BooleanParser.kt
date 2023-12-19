package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext

/**
 * Parser for a boolean argument
 *
 * @param A The type of the args this parser is used by
 * @constructor Creates a boolean parser
 *
 * @param builder The builder that defines this parser
 */
class BooleanParser(builder: BooleanParserBuilder) : ArgParser<Boolean, BooleanParser, BooleanParserBuilder>(builder) {
    override fun typeName(): String = "Boolean"

    private val internalParser = stringParser {
        name = builder.name
        description = builder.description
    }

    override fun parseValue(context: ParseContext): ParseResult<Boolean> {
        val stringResult = internalParser.parseValidValue(context)

        return if (stringResult is ParseResult.Success) {
            val result = stringResult.value.toBooleanStrictOrNull()

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
 * Builder for a [BooleanParser]
 *
 * @param A The args the parser will be a part of
 * @constructor Creates a boolean parser builder
 */
class BooleanParserBuilder : ParserBuilder<Boolean, BooleanParser, BooleanParserBuilder>("Boolean", "True or false") {
    init {
        this.completer = { listOf("true", "false") }
    }

    override fun build(): BooleanParser {
        return BooleanParser(this)
    }
}
