package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.args.NoArgs
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor

/**
 * A builder that configures and constructs a [MinecraftCommand]
 *
 * @property name The name of the command to build
 * @constructor Create an empty command builder
 */
class CommandBuilder(val name: String) {
    /**
     * The executors for this command
     */
    val executors = mutableListOf<CommandExecutor<*>>()

    /**
     * Build the command from this command builder
     *
     * @return The command that was built
     */
    fun build(): Command {
        return Command(
            CommandDefinition(
                name = name,
                executors = executors
            )
        )
    }

    /**
     * Add a runner to this command that does not use any arguments
     *
     * @param commandFunction The function to execute when this command is executed with this runner's arguments
     * @receiver The constructed command
     */
    fun runner(commandFunction: (CommandExecuteContext<NoArgs>) -> Boolean) = runner(::NoArgs, commandFunction)

    /**
     * Add a runner this command for a set of arguments
     *
     * @param A The type of the arguments this runner uses
     * @param arguments A constructor for this runner's arguments
     * @param commandFunction The function to execute when this command is executed with this runner's arguments
     * @receiver The constructed command
     * @receiver The constructed command
     */
    fun <A : CommandArgs<A>> runner(
        arguments: () -> A,
        commandFunction: (CommandExecuteContext<A>) -> Boolean
    ) {
        val executor = CommandExecutor(commandFunction, arguments)

        executors.add(executor)
    }
}
