package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.chatExtensionEnabled
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConnectionListener : Listener, KoinComponent {
    private val config: PluginConfig by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!config.chatExtensionEnabled()) {
            pluginCoroutineScope.launch {
                GameChatEvent.trigger {
                    content = "**${event.player.name}** joined the game"
                }
            }
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        if (!config.chatExtensionEnabled()) {
            pluginCoroutineScope.launch {
                GameChatEvent.trigger {
                    content = "**${event.player.name}** left the game"
                }
            }
        }
    }
}
