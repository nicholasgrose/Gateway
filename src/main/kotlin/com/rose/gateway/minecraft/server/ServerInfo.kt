package com.rose.gateway.minecraft.server

import org.bukkit.Bukkit

object ServerInfo {
    fun getPlayerList(): String {
        return Bukkit.getOnlinePlayers().joinToString(", ") { player -> player.name }
    }
}
