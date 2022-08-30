package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.chatExtensionEnabled
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.minecraft.chat.processing.ChatProcessor
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommandListener : Listener, KoinComponent {
    private val config: PluginConfig by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        if (config.chatExtensionEnabled()) {
            chatEventForCommand {
                CommandTextExtractor.processServerCommand(event.command)
            }
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        if (config.chatExtensionEnabled()) {
            chatEventForCommand {
                CommandTextExtractor.processPlayerCommand(event.message, event.player.name)
            }
        }
    }

    private fun chatEventForCommand(messageTextProvider: () -> String?) {
        pluginCoroutineScope.launch {
            val messageText = messageTextProvider() ?: return@launch
            val discordMessage = ChatProcessor.convertToDiscordMessage(messageText) ?: return@launch

            GameChatEvent.trigger(discordMessage)
        }
    }
}
