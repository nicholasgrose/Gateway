package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.chatExtensionEnabled
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.minecraft.chat.processing.discordMessage
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import com.rose.gateway.shared.concurrency.runBlockingIf
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.launch
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Listener that posts Minecraft chat messages in Discord
 *
 * @constructor Create empty Chat listener
 */
class ChatListener : Listener, KoinComponent {
    private val config: PluginConfig by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    /**
     * Posts a game chat to Discord
     *
     * [AsyncChatEvent] is used because that allows guaranteeing processing does not occur in the server's main thread
     *
     * To maintain compatibility with other chat plugins, Gateway uses [EventPriority.LOWEST] so that it goes first
     * This means that it will always be overwritten by other plugins' formatting until a better solution is found
     *
     * @param event The async chat event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncChatEvent) {
        runBlockingIf(config.chatExtensionEnabled() && event.isAsynchronous) {
            val messageText = PlainTextComponentSerializer.plainText().serialize(event.message())
            val message = discordMessage(messageText, event) ?: return@runBlockingIf

            pluginCoroutineScope.launch {
                GameChatEvent.trigger(message)
            }
        }
    }
}
