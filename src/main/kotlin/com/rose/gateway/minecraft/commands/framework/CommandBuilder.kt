package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.NoArgs
import com.rose.gateway.minecraft.commands.framework.subcommand.subcommandExecutor
import com.rose.gateway.shared.collections.builders.trieOf

class CommandBuilder(private val name: String) {
    var parent: CommandBuilder? = null
    val children = mutableListOf<Command>()
    private val executors = mutableListOf<CommandExecutor<*>>()

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

    fun runner(commandFunction: (CommandContext<NoArgs>) -> Boolean) = runner(::NoArgs, commandFunction)

    fun <A : CommandArgs<A>> runner(
        arguments: () -> A,
        commandFunction: (CommandContext<A>) -> Boolean
    ) {
        val executor = CommandExecutor(commandFunction, arguments)

        executors.add(executor)
    }

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
