package com.rose.gateway.minecraft.users

import com.rose.gateway.bot.presence.DynamicPresence
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object UserCount : Listener {
    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onJoin(event: PlayerJoinEvent) {
        DynamicPresence.playerCount += 1
        runBlocking {
            DynamicPresence.updatePresencePlayerCount()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerQuitEvent) {
        DynamicPresence.playerCount -= 1
        runBlocking {
            DynamicPresence.updatePresencePlayerCount()
        }
    }
}
