package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.data.parser.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

/**
 * Parser for a [Unit] type
 *
 * This represents a parser that does nothing and returns the same data inputted.
 *
 * @param A The type of the args this parser is for
 * @constructor Create a unit parser
 */
class UnitParser<A : CommandArgs<A>> : ArgParser<Unit, A, UnitParser<A>>(UnitParserBuilder()) {
    override fun typeName(): String = "Unit"

    override fun parseValue(context: ParseContext<A>): ParseResult<Unit, A> = ParseResult.Success(Unit, context)
}

/**
 * Builder for a [UnitParser]
 *
 * @param A The type of the args this is a builder for
 * @constructor Create a unit parser builder
 */
class UnitParserBuilder<A : CommandArgs<A>> : ParserBuilder<Unit, A, UnitParser<A>>() {
    override fun checkValidity() {
        return
    }

    override fun build(): UnitParser<A> = UnitParser()
}
