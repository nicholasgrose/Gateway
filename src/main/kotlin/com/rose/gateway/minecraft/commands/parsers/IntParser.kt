package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.data.parser.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

/**
 * Adds an int argument to these args
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The arg body
 * @receiver The builder for the parser
 * @return The built parser
 */
fun <A> CommandArgs<A>.int(body: IntParserBuilder<A>.() -> Unit): IntParser<A>
    where A : CommandArgs<A> =
    genericParser(::IntParserBuilder, body)

/**
 * Parser for an int argument
 *
 * @param A The type of the args this parser is used by
 * @constructor Creates an int parser
 *
 * @param builder The builder that defines this parser
 */
class IntParser<A : CommandArgs<A>>(builder: IntParserBuilder<A>) :
    ArgParser<Int, A, IntParser<A>>(builder) {
    override fun typeName(): String = "Int"

    private val internalParser =
        stringParser<A> {
            name = builder.name
            description = builder.description
        }

    override fun parseValue(context: ParseContext<A>): ParseResult<Int, A> {
        val stringResult = internalParser.parseValidValue(context)

        return if (stringResult is ParseResult.Success) {
            val result = stringResult.result.toIntOrNull()

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
class IntParserBuilder<A : CommandArgs<A>> : ParserBuilder<Int, A, IntParser<A>>() {
    override fun checkValidity() = Unit

    override fun build(): IntParser<A> {
        return IntParser(this)
    }
}
