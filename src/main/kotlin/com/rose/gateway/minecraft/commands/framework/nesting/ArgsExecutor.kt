package com.rose.gateway.minecraft.commands.framework.nesting

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor

/**
 * Executor for a set of referenced args and their internal command
 *
 * @param A The type of the referenced args
 * @param argsRef The reference to the args
 * @param internalCommand The command to wrap and execute
 */
fun <A : CommandArgs<A>> argsReferenceExecutor(
    argsRef: ArgsReference<A>,
    internalCommand: Command
) = CommandExecutor(
    ::argsReferenceRunner,
    ::ReferenceArgs
)

/**
 * Runner for a set of referenced args
 *
 * @param context The context this is executing in
 * @return Whether execution succeeded
 */
fun argsReferenceRunner(context: CommandExecuteContext<ReferenceArgs>): Boolean {
    TODO("Implement runner")
}
