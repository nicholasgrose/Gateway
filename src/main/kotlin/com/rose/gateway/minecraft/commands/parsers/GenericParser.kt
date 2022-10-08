package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

/**
 * Constructs a parser and adds it to the args as an argument
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
fun <T, A : CommandArgs<A>, B : ParserBuilder<T, A, P>, P : ArgParser<T, A, P>> CommandArgs<A>.genericParser(
    builderConstructor: () -> B,
    body: B.() -> Unit
): P {
    val arg = genericParserBuilder(builderConstructor, body)

    this.parsers.add(arg)

    return arg
}

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
fun <T, A : CommandArgs<A>, B : ParserBuilder<T, A, P>, P : ArgParser<T, A, P>> genericParserBuilder(
    builderConstructor: () -> B,
    body: B.() -> Unit
): P {
    val builder = builderConstructor()

    body(builder)

    return builder.buildAndCheck()
}
