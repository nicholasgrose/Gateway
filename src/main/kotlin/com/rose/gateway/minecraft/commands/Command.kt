package com.rose.gateway.minecraft.commands

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
        else {
            try {
                definition.runner(
                    CommandContext(
                        definition = definition,
                        sender = sender,
                        command = command,
                        label = label,
                        commandArguments = convertedArguments
                    )
                )
            } catch (e: Error) {
                sender.sendMessage(Component.text("Error: ${e.message}", TextColor.fromHexString("#FF0000")))
            }
        }

        return true
    }

    private fun sendArgumentErrorMessage(sender: CommandSender) {
        val message = "Arguments are incorrect.\nUsage:${definition.usage}"
        sender.sendMessage(message)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        TODO("Not yet implemented")
    }
}

data class CommandDefinition(
    val name: String,
    val usage: String,
    val childCommands: List<Command>,
    val checker: Checker,
    val runner: ((CommandContext) -> Unit),
    val completions: List<String>
)

data class CommandContext(
    val definition: CommandDefinition,
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val label: String,
    val commandArguments: List<*>
)

class Checker(val converters: Array<out ArgumentConverter<*>>) {
    fun convertArguments(arguments: Array<String>): List<*>? {
        if (converters.size != arguments.size) return null

        return converters.mapIndexed { index, converter ->
            converter.fromString(arguments[index]) ?: return@convertArguments null
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
            return Command(
                CommandDefinition(
                    name = builder.name,
                    usage = builder.generateUsage(),
                    checker = builder.checker,
                    runner = builder.commandRunner,
                    completions = builder.children.map { child -> child.definition.name }
                )
            )
        }
    }

    private var parent: CommandBuilder? = null
    private val children = mutableListOf<Command>()
    private var commandRunner: ((CommandContext) -> Unit)? = null
    private var checker: Checker = Checker(arrayOf())

    fun command(name: String, initializer: CommandBuilder.() -> Unit) {
        val newCommandBuilder = CommandBuilder(name).apply(initializer)
        newCommandBuilder.parent = this
        children.add(build(newCommandBuilder))
    }

    fun runner(vararg arguments: ArgumentConverter<*>, commandFunction: (CommandContext) -> Unit) {
        checker = Checker(arguments)
        commandRunner = commandFunction
    }

    private fun generateUsage(): String {
        val usageEnding = generateUsageEnding()
        val commandUsageParts = if (usageEnding.isEmpty()) mutableListOf() else mutableListOf(usageEnding)
        var currentBuilder: CommandBuilder? = this

        while (currentBuilder != null) {
            commandUsageParts.add(0, this.name)
            currentBuilder = this.parent
        }

        return commandUsageParts.joinToString(separator = " ", prefix = "/")
    }

    private fun generateUsageEnding(): String {
        return if (commandRunner == null) {
            if (children.isEmpty()) {
                ""
            } else {
                children.joinToString(separator = "|", prefix = "[", postfix = "]") { child -> child.definition.name }
            }
        } else {
            if (checker.converters.isEmpty()) {
                ""
            } else {
                checker.converters.joinToString(separator = " ") { converter -> converter.getName() }
            }
        }
    }
}

fun minecraftCommand(name: String, initializer: CommandBuilder.() -> Unit): Command {
    return CommandBuilder.build(CommandBuilder(name).apply(initializer))
}
