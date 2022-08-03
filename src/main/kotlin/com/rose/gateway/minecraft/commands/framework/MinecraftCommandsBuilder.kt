package com.rose.gateway.minecraft.commands.framework

import org.bukkit.plugin.java.JavaPlugin

class MinecraftCommandsBuilder(val plugin: JavaPlugin) {
    companion object {
        fun minecraftCommands(
            plugin: JavaPlugin,
            initializer: MinecraftCommandsBuilder.() -> Unit
        ): MinecraftCommands {
            val builder = MinecraftCommandsBuilder(plugin)

            builder.apply(initializer)

            return MinecraftCommands(plugin, builder.commands)
        }
    }

    private val commands = mutableListOf<Command>()

    fun command(name: String, initializer: CommandBuilder.() -> Unit) {
        val builder = CommandBuilder(name)

        builder.apply(initializer)

        commands.add(CommandBuilder.build(builder))
    }
}
