package com.rose.gateway.minecraft.commands.framework

import org.bukkit.plugin.java.JavaPlugin

class MinecraftCommandsBuilder(val plugin: JavaPlugin) {
    val commands = mutableListOf<Command>()

    fun command(name: String, initializer: CommandBuilder.() -> Unit) {
        val builder = CommandBuilder(name)

        builder.apply(initializer)

        commands.add(builder.build())
    }
}
