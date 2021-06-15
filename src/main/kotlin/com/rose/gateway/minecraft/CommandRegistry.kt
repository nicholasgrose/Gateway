package com.rose.gateway.minecraft

import com.rose.gateway.minecraft.commands.DiscordCommand
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistry {
    fun registerCommands(plugin: JavaPlugin) {
        plugin.getCommand("discord")?.setExecutor(DiscordCommand())
    }
}
