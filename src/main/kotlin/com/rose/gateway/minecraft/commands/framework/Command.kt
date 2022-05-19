package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

class Command(val definition: CommandDefinition) : CommandExecutor, TabCompleter {
    companion object {
        fun subcommandRunner(context: CommandContext): Boolean {
            val subcommand = context.commandArguments.first() as String
            val childCommand = context.definition.subcommands[subcommand]
            val arguments = context.rawCommandArguments

            return if (childCommand == null) false
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

    override fun onCommand(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<String>
    ): Boolean {
        var success = false

        for (executor in definition.executors) {
            val commandArguments = executor.argumentParser.parseAllArguments(args)

            if (commandArguments != null) {
                success = executor.executor(
                    CommandContext(
                        definition = definition,
                        sender = sender,
                        command = command,
                        label = label,
                        rawCommandArguments = args.toList(),
                        commandArguments = commandArguments
                    )
                )

                if (!success) break
            }
        }

        if (!success) sendArgumentErrorMessage(sender)

        return true
    }

    private fun sendArgumentErrorMessage(sender: CommandSender) {
        sender.sendMessage(definition.documentation)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        for (executor in definition.executors) {
            val tabCompletions = executor.argumentParser.getTabCompletions(
                TabCompletionContext(
                    sender = sender,
                    command = command,
                    alias = alias,
                    rawArguments = args.toList(),
                    parsedArguments = executor.argumentParser.parseArgumentSubset(args) ?: continue,
                    commandDefinition = definition
                )
            )

            if (tabCompletions != null) return tabCompletions
        }

        return listOf()
    }

    fun registerCommand(plugin: JavaPlugin) {
        plugin.getCommand(definition.name)!!.setExecutor(this)
    }
}
