package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor
import com.rose.gateway.minecraft.commands.parsers.UnitParser

/**
 * A builder that configures and constructs a [MinecraftCommand]
 *
 * @property name The name of the command to build
 * @constructor Create an empty command builder
 */
open class CommandBuilder(var name: String) {
    /**
     * The executors for this command
     */
    val executors = mutableListOf<CommandExecutor<*, *, *>>()

    /**
     * Build the command from this command builder
     *
     * @return The command that was built
     */
    fun build(): Command {
        return Command(this)
    }

    /**
     * Define that this command run a function when no more arguments are provided
     *
     * @param commandFunction The function to execute when this command is executed with this runner's arguments
     * @receiver The constructed command
     */
    fun executes(argParser: ArgParser<*, *, *> = UnitParser(), commandFunction: (CommandExecuteContext) -> Boolean) {
        val executor = CommandExecutor(commandFunction, argParser)

        executors.add(executor)
    }
}
