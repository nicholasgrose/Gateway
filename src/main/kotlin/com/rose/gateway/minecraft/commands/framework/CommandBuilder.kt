package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.NoArgs
import com.rose.gateway.shared.collections.builders.trieOf

class CommandBuilder(private val name: String) {
    companion object {
        private fun subcommandExecutor(children: Map<String, Command>): CommandExecutor<SubcommandArgs> =
            CommandExecutor(
                ::subcommandRunner,
                SubcommandArgs.forChildCommands(children)
            )

        fun build(builder: CommandBuilder): Command {
            val subcommandMap = builder.children.associateBy { child -> child.definition.name }
            val subcommandNames = trieOf(subcommandMap.keys)
            if (subcommandMap.isNotEmpty()) builder.executors.add(subcommandExecutor(subcommandMap))

            return Command(
                CommandDefinition(
                    name = builder.name,
                    baseCommand = builder.generateBuilderDocumentation(),
                    executors = builder.executors,
                    subcommands = subcommandMap,
                    subcommandNames = subcommandNames
                )
            )
        }
    }

    private var parent: CommandBuilder? = null
    private val children = mutableListOf<Command>()
    private val executors = mutableListOf<CommandExecutor<*>>()

    fun subcommand(name: String, initializer: CommandBuilder.() -> Unit) {
        val newCommandBuilder = CommandBuilder(name)

        newCommandBuilder.apply(initializer)
        newCommandBuilder.parent = this

        children.add(build(newCommandBuilder))
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
