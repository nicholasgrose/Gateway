package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.data.parser.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

/**
 * Adds a boolean argument to these args
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The arg body
 * @receiver The builder for the parser
 * @return The built parser
 */
fun <A : CommandArgs<A>> CommandArgs<A>.boolean(body: BooleanParserBuilder<A>.() -> Unit): BooleanParser<A> =
    genericParser(::BooleanParserBuilder, body)

/**
 * Parser for a boolean argument
 *
 * @param A The type of the args this parser is used by
 * @constructor Creates a boolean parser
 *
 * @param builder The builder that defines this parser
 */
class BooleanParser<A>(
    builder: BooleanParserBuilder<A>,
) : ArgParser<Boolean, A, BooleanParser<A>>(builder)
    where A : CommandArgs<A> {
    override fun typeName(): String = "Boolean"

    private val internalParser =
        stringParser<A> {
            name = builder.name
            description = builder.description
        }

    override fun parseValue(context: ParseContext<A>): ParseResult<Boolean, A> {
        val stringResult = internalParser.parseValidValue(context)

        return if (stringResult is ParseResult.Success) {
            val result = stringResult.result.toBooleanStrictOrNull()

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
class BooleanParserBuilder<A : CommandArgs<A>> : ParserBuilder<Boolean, A, BooleanParser<A>>() {
    init {
        this.completer = { listOf("true", "false") }
    }

    override fun checkValidity() = Unit

    override fun build(): BooleanParser<A> = BooleanParser(this)
}
