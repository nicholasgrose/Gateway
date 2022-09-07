package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.minecraft.chat.processing.ChatProcessor
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatListener : Listener, KoinComponent {
    private val config: PluginConfig by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncChatEvent) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(event.isAsynchronous, config) {
            val messageText = PlainTextComponentSerializer.plainText().serialize(event.message())
            val message =
                ChatProcessor.convertToDiscordMessage(messageText, event) ?: return@launchIfChatExtensionEnabled

            GameChatEvent.trigger(message)
        }
    }
}
