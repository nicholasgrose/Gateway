package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.converters.StringArg

class CommandBuilder(private val name: String) {
    companion object {
        fun build(builder: CommandBuilder): Command {
            val checker = getChecker(builder)

            return Command(
                CommandDefinition(
                    name = builder.name,
                    usage = generateUsage(builder, checker),
                    argumentParser = checker,
                    runner = getRunner(builder),
                    subcommands = builder.children.associateBy { child -> child.definition.name }
                )
            )
        }

        private fun getChecker(builder: CommandBuilder): ArgumentParser {
            return when {
                builder.argumentParser != null -> builder.argumentParser!!
                builder.commandRunner == null -> ArgumentParser(
                    arrayOf(StringArg("SUBCOMMAND")),
                    true
                )
                else -> ArgumentParser(arrayOf(), false)
            }
        }

        private fun generateUsage(builder: CommandBuilder, argumentParser: ArgumentParser): String {
            val usageEnding = generateUsageEnding(builder, argumentParser)
            val commandUsageParts = if (usageEnding.isEmpty()) mutableListOf() else mutableListOf(usageEnding)
            var currentBuilder: CommandBuilder? = builder

            while (currentBuilder != null) {
                commandUsageParts.add(0, currentBuilder.name)
                currentBuilder = currentBuilder.parent
            }

            return commandUsageParts.joinToString(separator = " ", prefix = "/")
        }

        private fun generateUsageEnding(builder: CommandBuilder, argumentParser: ArgumentParser): String {
            return if (builder.commandRunner == null) {
                if (builder.children.isEmpty()) {
                    ""
                } else {
                    builder.children.joinToString(
                        separator = " | ",
                        prefix = "[",
                        postfix = "]"
                    ) { child -> child.definition.name }
                }
            } else {
                if (argumentParser.converters.isEmpty()) {
                    ""
                } else {
                    argumentParser.converters.joinToString(separator = " ") { converter -> converter.getName() }
                }
            }
        }

        private fun getRunner(builder: CommandBuilder): (CommandContext) -> Boolean {
            return builder.commandRunner ?: Command::subcommandRunner
        }
    }

    private var parent: CommandBuilder? = null
    private val children = mutableListOf<Command>()
    private var commandRunner: ((CommandContext) -> Boolean)? = null
    private var argumentParser: ArgumentParser? = null

    fun subcommand(name: String, initializer: CommandBuilder.() -> Unit) {
        val newCommandBuilder = CommandBuilder(name)
        newCommandBuilder.parent = this
        newCommandBuilder.apply(initializer)
        children.add(build(newCommandBuilder))
    }

    fun runner(
        vararg arguments: Parser<*>,
        allowVariableNumberOfArguments: Boolean = false,
        commandFunction: (CommandContext) -> Boolean
    ) {
        argumentParser = ArgumentParser(arguments, allowVariableNumberOfArguments)
        commandRunner = commandFunction
    }
}
