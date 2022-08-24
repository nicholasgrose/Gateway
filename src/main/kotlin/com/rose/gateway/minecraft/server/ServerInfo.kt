package com.rose.gateway.minecraft.server

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Provides info about the server.
 */
object ServerInfo {
    val onlinePlayers: Collection<Player>
        get() = Bukkit.getOnlinePlayers()
    val playerCount: Int
        get() = onlinePlayers.size
}
