package com.rose.gateway.minecraft.users

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent
import com.rose.gateway.GatewayPlugin
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UserCount(val plugin: GatewayPlugin) : Listener {
    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onJoin(event: PlayerJoinEvent) {
        runBlocking {
            plugin.discordBot.presence.updatePresencePlayerCount()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerConnectionCloseEvent) {
        runBlocking {
            plugin.discordBot.presence.updatePresencePlayerCount()
        }
    }
}
