package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.minecraft.chat.processing.discordMessage
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Listener that posts Minecraft command events that must be displayed in Discord.
 *
 * @constructor Create a command listener.
 */
class CommandListener : Listener, KoinComponent {
    private val config: PluginConfig by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    /**
     * Posts a server command to Discord, if necessary.
     *
     * @param event The server command event.
     */
    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        postCommandInDiscord {
            discordTextForServerCommand(event.command)
        }
    }

    /**
     * Posts a player command to Discord, if necessary.
     *
     * @param event The player-command-preprocess event.
     */
    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        postCommandInDiscord {
            discordTextForPlayerCommand(event.message, event.player.name)
        }
    }

    /**
     * Posts a player command to Discord, if necessary.
     *
     * @param discordTextProvider Provider for the Discord text.
     */
    private fun postCommandInDiscord(discordTextProvider: () -> String?) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            val messageText = discordTextProvider() ?: return@launchIfChatExtensionEnabled
            val discordMessage = discordMessage(messageText) ?: return@launchIfChatExtensionEnabled

            GameChatEvent.trigger(discordMessage)
        }
    }
}
