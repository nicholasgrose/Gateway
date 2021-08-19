package com.rose.gateway.minecraft.server

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ServerInfo {
    fun playerListAsString(): String {
        return onlinePlayers().joinToString(", ") { player -> player.name }
    }

    private fun onlinePlayers(): Collection<Player> {
        return Bukkit.getOnlinePlayers()
    }

    fun playerCount(): Int {
        return onlinePlayers().size
    }
}
