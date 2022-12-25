package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.data.parser.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

class UnitParser<A> : ArgParser<Unit, A, UnitParser<A>>(UnitParserBuilder()) where
A : CommandArgs<A> {
    override fun typeName(): String = "Unit"

    override fun parseValue(context: ParseContext<A>): ParseResult<Unit, A> = ParseResult.Success(Unit, context)
}

class UnitParserBuilder<A : CommandArgs<A>> : ParserBuilder<Unit, A, UnitParser<A>>() {
    override fun checkValidity() {
        return
    }

    override fun build(): UnitParser<A> = UnitParser()
}
