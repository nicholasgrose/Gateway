package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.schema.ChatConfig
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
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        ChatConfig.ifEnabled {
            chatEventForCommand {
                CommandTextExtractor.processServerCommand(event.command)
            }
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        ChatConfig.ifEnabled {
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
