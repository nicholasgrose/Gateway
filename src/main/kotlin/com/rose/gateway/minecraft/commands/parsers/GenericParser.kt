package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder

fun <T, A : CommandArgs<A>, B : ParserBuilder<T, A, P>, P : ArgParser<T, A, P>> CommandArgs<A>.genericParser(
    builderConstructor: () -> B,
    body: B.() -> Unit
): P {
    val arg = genericParserBuilder(builderConstructor, body)

    this.parsers.add(arg)

    return arg
}

fun <T, A : CommandArgs<A>, B : ParserBuilder<T, A, P>, P : ArgParser<T, A, P>> genericParserBuilder(
    builderConstructor: () -> B,
    body: B.() -> Unit
): P {
    val builder = builderConstructor()

    body(builder)

    return builder.buildAndCheck()
}
