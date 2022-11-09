package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.data.ExecutorArgsPair
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

class MinecraftCommand(val command: Command) : org.bukkit.command.CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        bukkitCommand: org.bukkit.command.Command,
        label: String,
        args: Array<String>
    ): Boolean {
        val argList = args.toList()
        val commandResult = command.parseAndExecute(
            CommandContext(
                sender = sender,
                bukkitCommand = bukkitCommand,
                label = label,
                args = emptyArgs(argList)
            )
        )

        if (!commandResult.succeeded) sendUsages(sender, commandResult.rankedExecutors)

        return true
    }

    private fun sendUsages(sender: CommandSender, rankedExecutors: List<ExecutorArgsPair<*>>) {
        sender.sendMessage(
            "Usage:\n" +
                rankedExecutors.joinToString("\n") {
                    it.args.usages()
                        .joinToString("\n") { usage -> "/${command.definition.name} $usage" }
                }
        )
    }

    override fun onTabComplete(
        sender: CommandSender,
        bukkitCommand: org.bukkit.command.Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        val argList = args.toList()

        return command.parseAndComplete(
            TabCompletionContext(
                sender = sender,
                bukkitCommand = bukkitCommand,
                command = command,
                alias = alias,
                args = emptyArgs(argList)
            )
        )
    }

    /**
     * Registers this command with the provided plugin
     *
     * @param plugin The plugin to register this command with
     */
    fun registerCommand(plugin: JavaPlugin) {
        plugin.getCommand(command.definition.name)!!.setExecutor(this)
    }
}
