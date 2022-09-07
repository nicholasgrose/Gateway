package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.minecraft.chat.processing.ChatProcessor
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
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
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            chatEventForCommand {
                discordTextForServerCommand(event.command)
            }
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            chatEventForCommand {
                discordTextForPlayerCommand(event.message, event.player.name)
            }
        }
    }

    private fun chatEventForCommand(messageTextProvider: () -> String?) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            val messageText = messageTextProvider() ?: return@launchIfChatExtensionEnabled
            val discordMessage =
                ChatProcessor.convertToDiscordMessage(messageText) ?: return@launchIfChatExtensionEnabled

            GameChatEvent.trigger(discordMessage)
        }
    }
}
