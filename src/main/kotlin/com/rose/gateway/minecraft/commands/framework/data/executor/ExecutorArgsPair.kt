package com.rose.gateway.minecraft.commands.framework.data.executor

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs

/**
 * An executor paired with an instance of its associated args class
 *
 * @param A The type of the args the executor uses
 * @property executor The executor
 * @property args An instance of the executor's args
 * @constructor Create an executor-args pair
 */
data class ExecutorArgsPair<A : CommandArgs<A>>(
    val executor: CommandExecutor<A>,
    val args: A
) {
    companion object {
        /**
         * Creates an paired executor and args instance from a list of raw args
         *
         * @param A The type of the args the executor uses
         * @param executor The executor to create the pair for
         * @param rawArgs The raw arguments that are provided to the executor
         * @return The constructor executor-args pair
         */
        fun <A : CommandArgs<A>> forExecutor(executor: CommandExecutor<A>, rawArgs: List<String>): ExecutorArgsPair<A> {
            val args = executor.filledArgs(rawArgs)

            return ExecutorArgsPair(executor, args)
        }
    }
}
