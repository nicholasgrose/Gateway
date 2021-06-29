package com.rose.gateway.minecraft.commands

import com.rose.gateway.GatewayPlugin.Companion.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class Command(val definition: CommandDefinition) : CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<String>
    ): Boolean {
        val convertedArguments = definition.checker.convertArguments(args)

        if (convertedArguments == null) sendArgumentErrorMessage(sender)
        else try {
            val success = definition.runner(
                CommandContext(
                    definition = definition,
                    sender = sender,
                    command = command,
                    label = label,
                    rawCommandArguments = args.toList(),
                    commandArguments = convertedArguments
                )
            )

            if (!success) sendArgumentErrorMessage(sender)
        } catch (e: Error) {
            sender.sendMessage(Component.text("Error: ${e.message}", TextColor.fromHexString("#FF0000")))
            sendArgumentErrorMessage(sender)
        }

        return true
    }

    private fun sendArgumentErrorMessage(sender: CommandSender) {
        val message = "Correct usage: ${definition.usage}"
        sender.sendMessage(message)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        alias: String,
        args: Array<String>
    ): MutableList<String>? {
//        return definition.subcommandCompletions.keys.toMutableList()
        return null
    }

    fun registerCommand() {
        plugin.getCommand(definition.name)?.setExecutor(this)
    }
}

data class CommandDefinition(
    val name: String,
    val usage: String,
    val checker: Checker,
    val runner: ((CommandContext) -> Boolean),
    val subcommandCompletions: Map<String, Command>
)

data class CommandContext(
    val definition: CommandDefinition,
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val label: String,
    val rawCommandArguments: List<String>,
    val commandArguments: List<*>
)

class Checker(
    val converters: Array<out ArgumentConverter<*>>,
    private val variableArgumentNumberAllowed: Boolean
) {
    fun convertArguments(arguments: Array<String>): List<*>? {
        if (argumentCountIncorrect(arguments)) return null

        return converters.mapIndexed { index, converter ->
            converter.fromString(arguments[index]) ?: return@convertArguments null
        }
    }

    private fun argumentCountIncorrect(arguments: Array<String>): Boolean {
        return if (variableArgumentNumberAllowed) {
            converters.size > arguments.size
        } else {
            converters.size != arguments.size
        }
    }
}

interface ArgumentConverter<T> {
    fun fromString(string: String): T?
    fun getName(): String
}

class StringArg(private val name: String) : ArgumentConverter<String> {
    override fun fromString(string: String): String {
        return string
    }

    override fun getName(): String {
        return name
    }
}

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
                    subcommandCompletions = builder.children.associateBy { child -> child.definition.name }
                )
            )
        }

        private fun getChecker(builder: CommandBuilder): Checker {
            return when {
                builder.checker != null -> builder.checker!!
                builder.commandRunner == null -> Checker(
                    arrayOf(StringArg("subcommand")),
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
                val childCommand = context.definition.subcommandCompletions[subcommand]
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

    fun runner(vararg arguments: ArgumentConverter<*>, allowVariableNumberOfArguments: Boolean = false, commandFunction: (CommandContext) -> Boolean) {
        checker = Checker(arguments, allowVariableNumberOfArguments)
        commandRunner = commandFunction
    }
}

fun minecraftCommand(name: String, initializer: CommandBuilder.() -> Unit): Command {
    return CommandBuilder.build(CommandBuilder(name).apply(initializer))
}
