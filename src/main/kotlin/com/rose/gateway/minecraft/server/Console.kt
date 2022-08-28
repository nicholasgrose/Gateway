package com.rose.gateway.minecraft.server

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

/**
 * Provides functions that allow for interfacing with the Minecraft server console.
 */
object Console {
    /**
     * Runs a minecraft command in the console.
     *
     * @param command The command to run.
     * @return Whether execution succeeded.
     */
    fun runCommand(command: String): Boolean {
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }

    /**
     * Broadcasts a message to the entire server via the chat.
     *
     * @param message The message to broadcast.
     */
    fun broadcastMessage(message: Component) {
        Bukkit.getServer().broadcast(message)
    }
}
