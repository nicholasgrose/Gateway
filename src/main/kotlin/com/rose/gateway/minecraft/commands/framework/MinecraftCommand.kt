package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.args.NoArgs
import com.rose.gateway.minecraft.commands.framework.data.context.ArgsContext
import com.rose.gateway.minecraft.commands.framework.data.context.BukkitContext
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.ParserSpecificContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.data.executor.ExecutorArgsPair
import com.rose.gateway.minecraft.commands.parsers.UnitParser
import com.rose.gateway.shared.collections.bimap.bimapOf
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin

/**
 * A Minecraft command for Bukkit
 *
 * @property command The framework command to use for execution
 * @constructor Create a Minecraft command
 */
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
                args = ArgsContext(
                    raw = argList,
                    parsed = bimapOf()
                )
            )
        )

        if (!commandResult.succeeded) sendUsages(sender, commandResult.rankedExecutors)

        return true
    }

    /**
     * Sends all the valid command usages to the command sender
     *
     * @param sender The sender of the command to receive usages
     * @param rankedExecutors The most successful executors
     */
    private fun sendUsages(sender: CommandSender, rankedExecutors: List<ExecutorArgsPair<*>>) {
        sender.sendMessage(
            "Usage:\n" + rankedExecutors.joinToString("\n") {
                it.args.usages().joinToString("\n") { usage -> "/${command.definition.name} $usage" }
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
                command,
                ArgsContext(
                    argList,
                    bimapOf()
                ),
                BukkitContext.TabComplete(
                    sender = sender,
                    command = bukkitCommand,
                    alias = alias,
                    args = args
                ),
                ParserSpecificContext(
                    NoArgs.ref,
                    UnitParser()
                )
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
