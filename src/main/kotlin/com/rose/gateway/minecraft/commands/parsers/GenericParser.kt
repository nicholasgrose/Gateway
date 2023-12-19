package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder

/**
 * Constructs a parser
 *
 * @param T The type of the parser's result
 * @param A The type of the args the parser is a part of
 * @param B The type of the parser's builder
 * @param P The type of the parser
 * @param builderConstructor The constructor for the parser builder
 * @param body The body of the argument
 * @receiver The extended arguments
 * @receiver The parser's builder
 * @return The constructed parser
 */
fun <T, P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B>> genericParserBuilder(
    builderConstructor: () -> B,
    body: B.() -> Unit,
): P {
    val builder = builderConstructor()

    body(builder)

    return builder.build()
}
