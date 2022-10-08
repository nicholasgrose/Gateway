package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

fun <A : CommandArgs<A>> stringParser(body: StringParserBuilder<A>.() -> Unit): StringParser<A> =
    genericParserBuilder(::StringParserBuilder, body)

fun <A : CommandArgs<A>> CommandArgs<A>.string(body: StringParserBuilder<A>.() -> Unit): StringParser<A> =
    genericParser(::StringParserBuilder, body)

class StringParser<A : CommandArgs<A>>(val builder: StringParserBuilder<A>) :
    ArgParser<String, A, StringParser<A>>(builder) {
    override fun typeName(): String = "String"

    override fun parseValue(context: ParseContext<A>): ParseResult<String, A> {
        val args = context.arguments
        var currentIndex = context.currentIndex
        val result = args.rawArguments.getOrNull(currentIndex)

        return if (builder.hungry && result != null) {
            val results = mutableListOf<String>()
            var currentArg: String = result

            do {
                results.add(currentArg)
                currentIndex++
                currentArg = args.rawArguments.getOrNull(currentIndex) ?: break
            } while (true)

            ParseResult(
                succeeded = true,
                context = ParseContext(
                    arguments = args,
                    currentIndex = currentIndex + results.size
                ),
                result = results.joinToString(" ")
            )
        } else ParseResult(
            succeeded = result != null,
            context = ParseContext(
                arguments = args,
                currentIndex = currentIndex + 1
            ),
            result = result
        )
    }
}

class StringParserBuilder<A : CommandArgs<A>> : ParserBuilder<String, A, StringParser<A>>() {
    var hungry = false

    override fun checkValidity() = Unit

    override fun build(): StringParser<A> {
        return StringParser(this)
    }
}
