package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs

data class ExecutorArgsPair<A : CommandArgs<A>>(
    val executor: CommandExecutor<A>,
    val args: A
) {
    companion object {
        fun <A : CommandArgs<A>> forExecutor(executor: CommandExecutor<A>, rawArgs: List<String>): ExecutorArgsPair<A> {
            val args = executor.filledArgs(rawArgs)

            return ExecutorArgsPair(executor, args)
        }
    }
}
