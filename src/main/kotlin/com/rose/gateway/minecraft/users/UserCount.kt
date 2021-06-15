package com.rose.gateway.minecraft.users

import com.rose.gateway.bot.status.DynamicStatus
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object UserCount : Listener {
    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onJoin(event: PlayerJoinEvent) {
        DynamicStatus.playerCount += 1
        runBlocking {
            DynamicStatus.updateStatusPlayerCount()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerQuitEvent) {
        DynamicStatus.playerCount -= 1
        runBlocking {
            DynamicStatus.updateStatusPlayerCount()
        }
    }
}
