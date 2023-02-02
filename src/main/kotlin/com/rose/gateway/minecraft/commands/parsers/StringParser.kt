package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext

/**
 * Creates a string parser
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The parser body
 * @receiver The builder for the parser
 * @return The built parser
 */
fun <A : CommandArgs<A>> stringParser(body: StringParserBuilder<A>.() -> Unit): StringParser<A> =
    genericParserBuilder(::StringParserBuilder, body)

/**
 * Adds a string argument to these args
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The arg body
 * @receiver The builder for the parser
 * @return The built parser
 */
fun <A : CommandArgs<A>> CommandArgs<A>.string(body: StringParserBuilder<A>.() -> Unit): StringParser<A> =
    genericParser(::StringParserBuilder, body)

/**
 * Parser for a string argument
 *
 * @param A The type of the args this parser is used by
 * @constructor Creates a string parser
 *
 * @param builder The builder that defines this parser
 */
class StringParser<A : CommandArgs<A>>(override val builder: StringParserBuilder<A>) :
    ArgParser<String, A, StringParser<A>>(builder) {
    override fun typeName(): String = "String"

    override fun parseValue(context: ParseContext<A>): ParseResult<String, A> {
        val args = context.args
        var currentIndex = context.currentIndex
        val result = args.raw.getOrNull(currentIndex)

        return when {
            builder.hungry && result != null -> {
                val results = mutableListOf<String>()
                var currentArg: String = result

                do {
                    results.add(currentArg)
                    currentIndex++
                    currentArg = args.raw.getOrNull(currentIndex) ?: break
                } while (true)

                ParseResult.Success(
                    results.joinToString(" "),
                    ParseContext(
                        context.command,
                        args,
                        context.bukkit,
                        context.parserContext,
                        currentIndex = currentIndex + results.size
                    )
                )
            }

            result != null -> ParseResult.Success(
                result,
                ParseContext(
                    context.command,
                    args,
                    context.bukkit,
                    context.parserContext,
                    currentIndex + 1
                )
            )

            else -> ParseResult.Failure(
                ParseContext(
                    context.command,
                    args,
                    context.bukkit,
                    context.parserContext,
                    currentIndex + 1
                )
            )
        }
    }
}

/**
 * Builder for a [StringParser]
 *
 * @param A The args the parser will be a part of
 * @constructor Creates a string parser builder
 */
class StringParserBuilder<A : CommandArgs<A>> : ParserBuilder<String, A, StringParser<A>>() {
    /**
     * Whether additional arguments should be consumed as if they were part of one long string
     */
    var hungry = false

    override fun checkValidity() = Unit

    override fun build(): StringParser<A> {
        return StringParser(this)
    }
}
