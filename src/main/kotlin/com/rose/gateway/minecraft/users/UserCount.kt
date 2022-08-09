package com.rose.gateway.minecraft.users

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent
import com.rose.gateway.discord.bot.DiscordBot
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserCount : Listener, KoinComponent {
    val bot: DiscordBot by inject()

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onJoin(event: PlayerJoinEvent) {
        runBlocking {
            bot.presence.updatePresencePlayerCount()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerQuitEvent) {
        runBlocking {
            bot.presence.updatePresencePlayerCount()
        }
    }

    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerConnectionCloseEvent) {
        runBlocking {
            bot.presence.updatePresencePlayerCount()
        }
    }
}
