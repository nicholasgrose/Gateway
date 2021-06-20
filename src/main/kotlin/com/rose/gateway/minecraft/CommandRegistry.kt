package com.rose.gateway.minecraft

import com.rose.gateway.minecraft.commands.BotCommand
import com.rose.gateway.minecraft.commands.DiscordCommand
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistry {
    fun registerCommands(plugin: JavaPlugin) {
        plugin.getCommand(DiscordCommand.COMMAND_NAME)?.setExecutor(DiscordCommand())
        plugin.getCommand(BotCommand.COMMAND_NAME)?.setExecutor(BotCommand())
    }
}
