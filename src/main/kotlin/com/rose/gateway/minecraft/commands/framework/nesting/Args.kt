package com.rose.gateway.minecraft.commands.framework.nesting

import com.rose.gateway.minecraft.commands.framework.CommandBuilder
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference

/**
 * Defines a set of argument that may be referenced by its children
 *
 * @param args The args that will be used and referenced
 * @param builder The builder for the command when the args were supplied
 * @receiver The internally wrapped command
 */
fun <A : CommandArgs<A>> CommandBuilder.args(
    args: () -> A,
    builder: CommandBuilder.(ArgsReference<A>) -> Unit
) {
    val argsRef = ArgsReference(args)
    val internalCommand = CommandBuilder("args")
        .apply {
            builder(argsRef)
        }
        .build()

    executors.add(argsReferenceExecutor(argsRef, internalCommand))
}
