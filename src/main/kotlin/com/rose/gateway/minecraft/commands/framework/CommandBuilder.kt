package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.args.NoArgs
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference
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
     * Define that this command run a function when no more arguments are provided
     *
     * @param commandFunction The function to execute when this command is executed with this runner's arguments
     * @receiver The constructed command
     */
    fun executes(commandFunction: (CommandExecuteContext) -> Boolean) {
        val executor = CommandExecutor(commandFunction, ArgsReference(::NoArgs))

        executors.add(executor)
    }
}
