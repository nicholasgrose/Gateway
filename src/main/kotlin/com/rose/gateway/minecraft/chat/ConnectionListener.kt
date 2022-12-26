package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Listener that sends connection-related Minecraft events in Discord
 *
 * @constructor Create connection listener
 */
class ConnectionListener : Listener, KoinComponent {
    private val config: PluginConfig by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    /**
     * Posts a player-join message to Discord
     *
     * @param event The player-join event
     */
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            GameChatEvent.trigger {
                content = "**${event.player.name}** joined the game"
            }
        }
    }

    /**
     * Posts a player-leave message to Discord
     *
     * @param event The player-quit event
     */
    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            GameChatEvent.trigger {
                content = "**${event.player.name}** left the game"
            }
        }
    }
}
