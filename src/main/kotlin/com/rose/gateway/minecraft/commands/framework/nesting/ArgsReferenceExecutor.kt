package com.rose.gateway.minecraft.commands.framework.nesting

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor

/**
 * Executor for a set of referenced args and their internal command
 *
 * @param A The type of the referenced args
 * @param containedArgsRef The reference to the args
 * @param internalCommand The command to wrap and execute
 */
fun <A : CommandArgs<A>> argsReferenceExecutor(
    containedArgsRef: ArgsReference<A>,
    internalCommand: Command
): CommandExecutor<ReferenceArgs<A>> {
    val referenceArgsRef = ArgsReference(ReferenceArgs.forArgs(containedArgsRef, internalCommand))

    return CommandExecutor(
        { context ->
            val referenceArgs = context.argsFor(referenceArgsRef)

            internalCommand.execute(context, referenceArgs.rankedExecutors).succeeded
        },
        referenceArgsRef
    )
}
