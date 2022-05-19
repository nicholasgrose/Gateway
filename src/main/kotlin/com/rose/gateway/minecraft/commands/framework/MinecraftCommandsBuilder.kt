package com.rose.gateway.minecraft.commands.framework

import org.bukkit.plugin.java.JavaPlugin

class MinecraftCommandsBuilder(val plugin: JavaPlugin) {
    companion object {
        fun minecraftCommands(
            plugin: JavaPlugin,
            initializer: MinecraftCommandsBuilder.() -> Unit
        ): MinecraftCommands {
            return build(plugin, MinecraftCommandsBuilder(plugin).apply(initializer))
        }

        private fun build(plugin: JavaPlugin, builder: MinecraftCommandsBuilder): MinecraftCommands {
            return MinecraftCommands(plugin, builder.commands)
        }
    }

    val commands = mutableListOf<Command>()

    fun command(name: String, initializer: CommandBuilder.() -> Unit) {
        commands.add(CommandBuilder.build(CommandBuilder(name).apply(initializer)))
    }
}
