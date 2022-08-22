package com.rose.gateway.minecraft.users

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent
import com.rose.gateway.discord.bot.presence.BotPresence
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class UserCount : Listener {
    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onJoin(event: PlayerJoinEvent) {
        runBlocking {
            BotPresence.updatePresencePlayerCount()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerQuitEvent) {
        runBlocking {
            BotPresence.updatePresencePlayerCount()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerConnectionCloseEvent) {
        runBlocking {
            BotPresence.updatePresencePlayerCount()
        }
    }
}
