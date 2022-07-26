package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor
import com.rose.gateway.minecraft.commands.framework.runner.NoArguments
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import com.rose.gateway.shared.collections.builders.trieOf

class CommandBuilder(private val name: String) {
    companion object {
        private fun subcommandExecutor(children: Map<String, Command>): CommandExecutor<SubcommandArguments> =
            CommandExecutor(
                ::subcommandRunner,
                SubcommandArguments.forChildCommands(children)
            )

        fun build(builder: CommandBuilder): Command {
            val subcommandMap = builder.children.associateBy { child -> child.definition.name }
            val subcommandNames = trieOf(subcommandMap.keys)
            if (subcommandMap.isNotEmpty()) builder.executors.add(subcommandExecutor(subcommandMap))

            return Command(
                CommandDefinition(
                    name = builder.name,
                    documentation = builder.generateBuilderDocumentation(),
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

    fun runner(commandFunction: (CommandContext<NoArguments>) -> Boolean) = runner(::NoArguments, commandFunction)

    fun <A : RunnerArguments<A>> runner(
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
