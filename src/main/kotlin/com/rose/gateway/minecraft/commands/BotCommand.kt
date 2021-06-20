package com.rose.gateway.minecraft.commands

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class BotCommand : CommandExecutor, TabCompleter {
    companion object {
        const val COMMAND_NAME = "bot"
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        Logger.log("${sender.name} restarted the Discord bot!")
        GatewayPlugin.plugin.restartBot()

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (command.name == COMMAND_NAME && args.size <= 1) {
            return mutableListOf("restart")
        }

        return null
    }
}
