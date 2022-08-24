package com.rose.gateway.minecraft.server

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
}
