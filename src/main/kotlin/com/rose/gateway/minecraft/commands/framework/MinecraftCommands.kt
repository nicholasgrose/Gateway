package com.rose.gateway.minecraft.commands.framework

import org.bukkit.plugin.java.JavaPlugin

fun minecraftCommands(
    plugin: JavaPlugin,
    initializer: MinecraftCommandsBuilder.() -> Unit
): MinecraftCommands {
    val builder = MinecraftCommandsBuilder(plugin)

    builder.apply(initializer)

    return MinecraftCommands(plugin, builder.commands)
}

class MinecraftCommands(private val plugin: JavaPlugin, private val commands: List<Command>) {
    fun registerCommands() {
        commands.forEach { command -> command.registerCommand(plugin) }
    }
}
