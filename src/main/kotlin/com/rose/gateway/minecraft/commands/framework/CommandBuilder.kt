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
                    checker = checker,
                    runner = getRunner(builder),
                    subcommands = builder.children.associateBy { child -> child.definition.name }
                )
            )
        }

        private fun getChecker(builder: CommandBuilder): Checker {
            return when {
                builder.checker != null -> builder.checker!!
                builder.commandRunner == null -> Checker(
                    arrayOf(StringArg("SUBCOMMAND")),
                    true
                )
                else -> Checker(arrayOf(), false)
            }
        }

        private fun generateUsage(builder: CommandBuilder, checker: Checker): String {
            val usageEnding = generateUsageEnding(builder, checker)
            val commandUsageParts = if (usageEnding.isEmpty()) mutableListOf() else mutableListOf(usageEnding)
            var currentBuilder: CommandBuilder? = builder

            while (currentBuilder != null) {
                commandUsageParts.add(0, currentBuilder.name)
                currentBuilder = currentBuilder.parent
            }

            return commandUsageParts.joinToString(separator = " ", prefix = "/")
        }

        private fun generateUsageEnding(builder: CommandBuilder, checker: Checker): String {
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
                if (checker.converters.isEmpty()) {
                    ""
                } else {
                    checker.converters.joinToString(separator = " ") { converter -> converter.getName() }
                }
            }
        }

        private fun getRunner(builder: CommandBuilder): (CommandContext) -> Boolean {
            return builder.commandRunner ?: createRunner()
        }

        private fun createRunner(): (CommandContext) -> Boolean {
            return { context ->
                val subcommand = context.commandArguments[0] as String
                val childCommand = context.definition.subcommands[subcommand]
                val arguments = context.rawCommandArguments

                if (childCommand == null) false
                else {
                    childCommand.onCommand(
                        sender = context.sender,
                        command = context.command,
                        label = context.label,
                        args = arguments.subList(1, arguments.size).toTypedArray()
                    )
                    true
                }
            }
        }
    }

    private var parent: CommandBuilder? = null
    private val children = mutableListOf<Command>()
    private var commandRunner: ((CommandContext) -> Boolean)? = null
    private var checker: Checker? = null

    fun command(name: String, initializer: CommandBuilder.() -> Unit) {
        val newCommandBuilder = CommandBuilder(name)
        newCommandBuilder.parent = this
        newCommandBuilder.apply(initializer)
        children.add(build(newCommandBuilder))
    }

    fun runner(
        vararg arguments: ArgumentConverter<*>,
        allowVariableNumberOfArguments: Boolean = false,
        commandFunction: (CommandContext) -> Boolean
    ) {
        checker = Checker(arguments, allowVariableNumberOfArguments)
        commandRunner = commandFunction
    }
}
