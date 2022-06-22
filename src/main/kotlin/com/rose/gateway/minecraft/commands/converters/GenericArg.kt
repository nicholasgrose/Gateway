package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

fun <T, A : RunnerArguments<A>, B : ArgBuilder<T, A, R>, R : RunnerArg<T, A, R>> RunnerArguments<A>.genericParser(
    builderConstructor: () -> B,
    body: B.() -> Unit
): R {
    val builder = builderConstructor()

    body(builder)

    val arg = builder.buildAndCheck()

    this.parsers.add(arg)

    return arg
}
