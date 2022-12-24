package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.context.BukkitContext
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.ExecutorArgsPair
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
            CommandExecuteContext(
                bukkit = BukkitContext.CommandExecute(
                    sender = sender,
                    command = bukkitCommand,
                    label = label,
                    args = args
                ),
                command = command,
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
            TabCompleteContext(
                bukkit = BukkitContext.TabComplete(
                    sender = sender,
                    command = bukkitCommand,
                    alias = alias,
                    args = args
                ),
                command = command,
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
