package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.NoArgs
import com.rose.gateway.minecraft.commands.framework.subcommand.subcommandExecutor
import com.rose.gateway.shared.collections.builders.trieOf

/**
 * A builder that configures and constructs a [Command]
 *
 * @property name The name of the command to build
 * @constructor Create an empty command builder
 */
class CommandBuilder(private val name: String) {
    var parent: CommandBuilder? = null
    val children = mutableListOf<Command>()
    private val executors = mutableListOf<CommandExecutor<*>>()

    /**
     * Build the command from this command builder
     *
     * @return The command that was built
     */
    fun build(): Command {
        val subcommandMap = children.associateBy { child -> child.definition.name }
        val subcommandNames = trieOf(subcommandMap.keys)
        if (subcommandMap.isNotEmpty()) executors.add(subcommandExecutor(subcommandMap))

        return Command(
            CommandDefinition(
                name = name,
                baseCommand = generateBuilderDocumentation(),
                executors = executors,
                subcommands = subcommandMap,
                subcommandNames = subcommandNames
            )
        )
    }

    /**
     * Add a runner to this command that does not use any arguments
     *
     * @param commandFunction The function to execute when this command is executed with this runner's arguments
     * @receiver The constructed command
     */
    fun runner(commandFunction: (CommandContext<NoArgs>) -> Boolean) = runner(::NoArgs, commandFunction)

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
        commandFunction: (CommandContext<A>) -> Boolean
    ) {
        val executor = CommandExecutor(commandFunction, arguments)

        executors.add(executor)
    }

    /**
     * Generate usage documentation for this builder
     *
     * @return The generated documentation
     */
    private fun generateBuilderDocumentation(): String {
        var topParent = this
        var nextParent = topParent.parent

        while (nextParent != null) {
            topParent = nextParent
            nextParent = topParent.parent
        }

        return "/${topParent.name}"
    }
}
