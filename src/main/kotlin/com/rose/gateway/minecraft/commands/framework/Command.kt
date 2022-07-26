package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

class Command(val definition: CommandDefinition) : CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<String>
    ): Boolean {
        var succeeded = false

        for (executor in definition.executors) {
            val result = executor.tryExecute(
                definition = definition,
                sender = sender,
                command = command,
                label = label,
                rawArguments = args
            )

            if (result != null) {
                succeeded = result

                if (!succeeded) break
            }
        }

        if (!succeeded) sender.sendMessage(
            "Usage:\n" +
                definition.executors.joinToString("\n") {
                    definition.documentation + " " + it.arguments(args).documentation()
                }
        )

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        for (executor in definition.executors) {
            val tabCompletions = executor.completions(sender, command, alias, definition, args)

            if (tabCompletions != null) return tabCompletions
        }

        return listOf()
    }

    fun registerCommand(plugin: JavaPlugin) {
        plugin.getCommand(definition.name)!!.setExecutor(this)
    }
}
