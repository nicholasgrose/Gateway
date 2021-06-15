package com.rose.gateway.minecraft.server

import org.bukkit.Bukkit

object Console {
    fun runCommand(command: String): Boolean {
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}
