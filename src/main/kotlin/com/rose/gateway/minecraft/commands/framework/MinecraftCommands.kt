package com.rose.gateway.minecraft.commands.framework

import org.bukkit.plugin.java.JavaPlugin

/**
 * Create a set of Minecraft commands for a plugin
 *
 * @param initializer The settings for these minecraft commands
 * @receiver The minecraft commands builder
 * @return The constructed Minecraft commands
 */
fun minecraftCommands(initializer: MinecraftCommandsBuilder.() -> Unit): MinecraftCommands {
    val builder = MinecraftCommandsBuilder()

    builder.apply(initializer)

    return builder.build()
}

/**
 * A group of Minecraft commands
 *
 * @property builder The builder for these commands
 * @constructor Create minecraft commands for some commands
 */
class MinecraftCommands(private val builder: MinecraftCommandsBuilder) {
    /**
     * Register these commands with a plugin
     *
     * @param plugin The plugin to register these commands with
     */
    fun registerCommands(plugin: JavaPlugin) {
        builder.commands.forEach { command -> command.registerCommand(plugin) }
    }
}
