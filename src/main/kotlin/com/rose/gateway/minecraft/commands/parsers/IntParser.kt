package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

fun <A : CommandArgs<A>> CommandArgs<A>.int(body: IntParserBuilder<A>.() -> Unit): IntParser<A> =
    genericParser(::IntParserBuilder, body)

class IntParser<A : CommandArgs<A>>(builder: IntParserBuilder<A>) :
    ArgParser<Int, A, IntParser<A>>(builder) {
    override fun typeName(): String = "Int"

    private val internalParser = stringParser<A> {
        name = builder.name
        description = builder.description
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<Int, A> {
        val stringResult = internalParser.parseValidValue(context)
        val result = stringResult.result?.toInt()

        return ParseResult(
            succeeded = result != null,
            context = stringResult.context,
            result = result
        )
    }
}

class IntParserBuilder<A : CommandArgs<A>> : ParserBuilder<Int, A, IntParser<A>>() {
    override fun checkValidity() = Unit

    override fun build(): IntParser<A> {
        return IntParser(this)
    }
}
