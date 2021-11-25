package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.CommandArgument.Companion.subcommandCompleter
import com.rose.gateway.minecraft.commands.framework.converters.StringArg
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor
import com.rose.gateway.shared.collections.trie.Trie

class CommandBuilder(private val name: String) {
    companion object {
        private val subcommandExecutor = CommandExecutor(
            Command::subcommandRunner,
            ArgumentParser(arrayOf(StringArg("SUBCOMMAND", ::subcommandCompleter)), true)
        )

        fun build(builder: CommandBuilder): Command {
            if (builder.executors.isEmpty()) builder.executors.add(subcommandExecutor)
            val subcommandNames = Trie()
            subcommandNames.addAll(builder.children.map { child -> child.definition.name })

            return Command(
                CommandDefinition(
                    name = builder.name,
                    documentation = generateBuilderDocumentation(builder),
                    executors = builder.executors,
                    subcommands = builder.children.associateBy { child -> child.definition.name },
                    subcommandNames = subcommandNames
                )
            )
        }

        private fun generateBuilderDocumentation(builder: CommandBuilder): String {
            val usages = builder.executors.joinToString(separator = "\n") { executor ->
                generateExecutorUsage(executor, builder)
            }

            return if (builder.executors.size == 1) {
                "Correct Usage: $usages"
            } else {
                "Correct Usages:\n$usages"
            }
        }

        private fun generateExecutorUsage(executor: CommandExecutor, builder: CommandBuilder): String {
            val usageEnding =
                if (usesSubcommandRunner(builder)) generateSubcommandUsageEnding(builder)
                else generateUsageEnding(executor)
            val commandUsageParts = if (usageEnding.isEmpty()) mutableListOf() else mutableListOf(usageEnding)
            var currentBuilder: CommandBuilder? = builder

            while (currentBuilder != null) {
                commandUsageParts.add(0, currentBuilder.name)
                currentBuilder = currentBuilder.parent
            }

            return commandUsageParts.joinToString(separator = " ", prefix = "/")
        }

        private fun usesSubcommandRunner(builder: CommandBuilder): Boolean {
            return builder.executors.size == 1 && builder.executors.first() == subcommandExecutor
        }

        private fun generateSubcommandUsageEnding(builder: CommandBuilder): String {
            return builder.children.joinToString(
                separator = " | ",
                prefix = "[",
                postfix = "]"
            ) { child -> child.definition.name }
        }

        private fun generateUsageEnding(executor: CommandExecutor): String {
            return if (executor.argumentParser.converters.isEmpty()) ""
            else executor.argumentParser.converters.joinToString(
                separator = "] [",
                prefix = "[",
                postfix = "]"
            ) { argument -> "${argument.getName()}=${argument.getTypeName()}" }
        }
    }

    private var parent: CommandBuilder? = null
    private val children = mutableListOf<Command>()
    private val executors = mutableListOf<CommandExecutor>()

    fun subcommand(name: String, initializer: CommandBuilder.() -> Unit) {
        val newCommandBuilder = CommandBuilder(name)
        newCommandBuilder.parent = this
        newCommandBuilder.apply(initializer)
        children.add(build(newCommandBuilder))
    }

    fun runner(
        vararg arguments: CommandArgument<*>,
        allowVariableNumberOfArguments: Boolean = false,
        commandFunction: (CommandContext) -> Boolean
    ) {
        executors.add(CommandExecutor(commandFunction, ArgumentParser(arguments, allowVariableNumberOfArguments)))
    }
}
