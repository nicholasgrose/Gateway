package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.chatExtensionEnabled
import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.discord.text.discordBoldSafe
import com.rose.gateway.minecraft.chat.processing.ChatProcessor
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatListener : Listener, KoinComponent {
    val config: PluginConfig by inject()
    val bot: DiscordBot by inject()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncChatEvent) {
        if (!(config.chatExtensionEnabled() && event.isAsynchronous)) return

        val messageText = PlainTextComponentSerializer.plainText().serialize(event.message())

        runBlocking {
            val message = ChatProcessor.convertToDiscordMessage(messageText, event) ?: return@runBlocking
            bot.bot?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!config.chatExtensionEnabled()) return

        runBlocking {
            bot.bot?.send(
                GameChatEvent {
                    content = "**${event.player.name}** joined the game"
                }
            )
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        if (!config.chatExtensionEnabled()) return

        runBlocking {
            bot.bot?.send(
                GameChatEvent {
                    content = "**${event.player.name}** left the game"
                }
            )
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        if (!config.chatExtensionEnabled()) return

        val deathMessage = event.deathMessage() ?: return
        val plainTextMessage = PlainTextComponentSerializer.plainText().serialize(deathMessage)
            .replaceFirst(event.player.name, "**${event.player.name.discordBoldSafe()}**")

        runBlocking {
            bot.bot?.send(
                GameChatEvent {
                    content = plainTextMessage
                }
            )
        }
    }

    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        if (!config.chatExtensionEnabled()) return

        val advancementMessage = event.message() ?: return
        val advancementText = PlainTextComponentSerializer.plainText().serialize(advancementMessage)
            .replaceFirst(event.player.name, "**${event.player.name.discordBoldSafe()}**")

        runBlocking {
            bot.bot?.send(
                GameChatEvent {
                    content = advancementText
                }
            )
        }
    }
}
