package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import com.rose.gateway.shared.collections.builders.trieOf

class CommandBuilder(private val name: String) {
    companion object {
        private fun subcommandExecutor(subcommands: List<String>): CommandExecutor<SubcommandArguments> =
            CommandExecutor(
                ::subcommandRunner,
                SubcommandArguments.forSubcommands(subcommands)
            )

        fun build(builder: CommandBuilder): Command {
            val subCommands = builder.children.map { child -> child.definition.name }
            val subcommandNames = trieOf(subCommands)
            if (subCommands.isNotEmpty()) builder.executors.add(subcommandExecutor(subCommands))

            return Command(
                CommandDefinition(
                    name = builder.name,
                    documentation = builder.generateBuilderDocumentation(),
                    executors = builder.executors,
                    subcommands = builder.children.associateBy { child -> child.definition.name },
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

    fun <A : RunnerArguments<A>> runner(
        arguments: () -> A,
        commandFunction: (CommandContext<A>) -> Boolean
    ) {
        val executor = CommandExecutor(commandFunction, arguments)

        executors.add(executor)
    }

    private fun generateBuilderDocumentation(): String {
        val usages = executors.joinToString(separator = "\n") { executor ->
            generateExecutorUsage(executor)
        }

        return if (executors.size == 1) {
            "Correct Usage: $usages"
        } else {
            "Correct Usages:\n$usages"
        }
    }

    private fun generateExecutorUsage(executor: CommandExecutor<*>): String {
        val commandUsageParts = mutableListOf<String>()
        var currentBuilder: CommandBuilder? = this

        while (currentBuilder != null) {
            commandUsageParts.add(currentBuilder.name)
            currentBuilder = currentBuilder.parent
        }

        commandUsageParts.add(executor.generateUsageDocumentation())

        return commandUsageParts.joinToString(separator = " ", prefix = "/")
    }
}
