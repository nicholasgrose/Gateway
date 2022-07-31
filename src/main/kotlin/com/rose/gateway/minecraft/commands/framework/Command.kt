package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandDefinition
import com.rose.gateway.minecraft.commands.framework.data.CommandExecutor
import com.rose.gateway.shared.collections.builders.trieOf
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

class Command(val definition: CommandDefinition) : org.bukkit.command.CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<String>
    ): Boolean {
        val mostSuccessfulExecutors = determineMostSuccessfulExecutors(args)
        val chosenExecutor = mostSuccessfulExecutors.firstOrNull()

        val succeeded = if (chosenExecutor == null) {
            false
        } else {
            chosenExecutor.tryExecute(
                definition = definition,
                sender = sender,
                command = command,
                label = label,
                rawArguments = args
            ) ?: false
        }

        if (!succeeded) sender.sendMessage(
            "Usage:\n" +
                definition.executors.joinToString("\n") {
                    it.arguments(args).usages()
                        .joinToString("\n") { usage -> "${definition.baseCommand} $usage" }
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
        val mostSuccessfulExecutors = determineMostSuccessfulExecutors(args)

        val tabCompletions = trieOf(
            mostSuccessfulExecutors
                .map {
                    it.completions(sender, command, alias, definition, args)
                }.flatten()
        )

        return tabCompletions.searchOrGetAll(args.last()).sorted()
    }

    /**
     * Determines which executors are considered the most successful.
     *
     * Success is defined as either being successful or having the most arguments successfully parsed.
     * The returned executors are in the same order they were defined.
     *
     * @param rawArgs The incoming arguments to be parsed.
     * @return List of executors in order of definition.
     */
    private fun determineMostSuccessfulExecutors(rawArgs: Array<String>): MutableList<CommandExecutor<*>> {
        val mostSuccessfulExecutors = mutableListOf<CommandExecutor<*>>()
        var successThreshold = 0
        var executorsMustBeSuccessful = false

        for (executor in definition.executors) {
            val argResult = executor.arguments(rawArgs)
            val argsParsed = argResult.argsParsed()

            when {
                argResult.valid() -> {
                    if (!executorsMustBeSuccessful) {
                        mostSuccessfulExecutors.clear()
                        executorsMustBeSuccessful = true
                    }

                    mostSuccessfulExecutors.add(executor)
                }

                executorsMustBeSuccessful && !argResult.valid() -> continue
                argsParsed == successThreshold -> mostSuccessfulExecutors.add(executor)
                argsParsed > successThreshold -> {
                    successThreshold = argsParsed
                    mostSuccessfulExecutors.clear()
                    mostSuccessfulExecutors.add(executor)
                }
            }
        }

        return mostSuccessfulExecutors
    }

    fun registerCommand(plugin: JavaPlugin) {
        plugin.getCommand(definition.name)!!.setExecutor(this)
    }
}
