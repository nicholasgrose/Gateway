package com.rose.gateway.minecraft.commands.framework

import org.bukkit.plugin.java.JavaPlugin

class MinecraftCommands(private val plugin: JavaPlugin, private val commands: List<Command>) {
    fun registerCommands() {
        commands.forEach { command -> command.registerCommand(plugin) }
    }
}
