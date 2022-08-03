package com.rose.gateway.minecraft.chat

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.bot.extensions.chat.GameChatEvent
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.chat.processing.MinecraftMessageProcessor
import com.rose.gateway.shared.configurations.chatExtensionEnabled
import com.rose.gateway.shared.discord.discordBoldSafe
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerCommandEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatListener : Listener, KoinComponent {
    val config: PluginConfiguration by inject()
    val bot: DiscordBot by inject()

    private val minecraftMessageProcessor = MinecraftMessageProcessor()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(event: AsyncChatEvent) {
        if (!(config.chatExtensionEnabled() && event.isAsynchronous)) return

        val messageText = PlainTextComponentSerializer.plainText().serialize(event.message())

        runBlocking {
            val message = minecraftMessageProcessor.convertToDiscordMessage(messageText, event) ?: return@runBlocking
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
    fun onServerCommand(event: ServerCommandEvent) {
        if (!config.chatExtensionEnabled()) return

        val messageText = DisplayCommandProcessor.processServerCommand(event.command)

        if (messageText.isEmpty()) return

        runBlocking {
            val message = minecraftMessageProcessor.convertToDiscordMessage(messageText) ?: return@runBlocking
            bot.bot?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        if (!config.chatExtensionEnabled()) return

        val messageText = DisplayCommandProcessor.processPlayerCommand(event.message, event.player.name)

        if (messageText.isEmpty()) return

        runBlocking {
            val message = minecraftMessageProcessor.convertToDiscordMessage(messageText) ?: return@runBlocking
            bot.bot?.send(GameChatEvent(message))
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
