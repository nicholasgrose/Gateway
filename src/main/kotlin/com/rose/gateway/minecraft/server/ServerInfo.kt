package com.rose.gateway.minecraft.server

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Provides info about the server
 */
object ServerInfo {
    /**
     * The players currently online on the server
     */
    val onlinePlayers: Collection<Player>
        get() = Bukkit.getOnlinePlayers()

    /**
     * The number of players currently online
     */
    val playerCount: Int
        get() = onlinePlayers.size
}
