package com.rose.gateway.minecraft.chat

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.extensions.chat.GameChatEvent
import com.rose.gateway.shared.configurations.BotConfiguration.chatExtensionEnabled
import com.rose.gateway.shared.discord.StringModifiers.discordBoldSafe
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.text.PaperComponents
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerCommandEvent

class ChatListener(val plugin: GatewayPlugin) : Listener {
    private val minecraftMessageProcessor = MinecraftMessageProcessor(plugin)

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        if (!plugin.configuration.chatExtensionEnabled()) return

        val messageText = PlainTextComponentSerializer.plainText().serialize(event.message())

        runBlocking {
            val message = minecraftMessageProcessor.convertToDiscordMessage(messageText, event) ?: return@runBlocking
            plugin.discordBot.bot?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!plugin.configuration.chatExtensionEnabled()) return

        runBlocking {
            plugin.discordBot.bot?.send(GameChatEvent {
                content = "**${event.player.name}** joined the game"
            })
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        if (!plugin.configuration.chatExtensionEnabled()) return

        runBlocking {
            plugin.discordBot.bot?.send(GameChatEvent {
                content = "**${event.player.name}** left the game"
            })
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        if (!plugin.configuration.chatExtensionEnabled()) return

        val deathMessage = event.deathMessage() ?: return
        val plainTextMessage = PaperComponents.plainTextSerializer().serialize(deathMessage)
            .replaceFirst(event.player.name, "**${event.player.name.discordBoldSafe()}**")

        runBlocking {
            plugin.discordBot.bot?.send(GameChatEvent {
                content = plainTextMessage
            })
        }
    }

    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        if (!plugin.configuration.chatExtensionEnabled()) return

        val messageText = DisplayCommandProcessor.processServerCommand(event.command)

        if (messageText.isEmpty()) return

        runBlocking {
            val message = minecraftMessageProcessor.convertToDiscordMessage(messageText) ?: return@runBlocking
            plugin.discordBot.bot?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        if (!plugin.configuration.chatExtensionEnabled()) return

        val messageText = DisplayCommandProcessor.processPlayerCommand(event.message, event.player.name)

        if (messageText.isEmpty()) return

        runBlocking {
            val message = minecraftMessageProcessor.convertToDiscordMessage(messageText) ?: return@runBlocking
            plugin.discordBot.bot?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        if (!plugin.configuration.chatExtensionEnabled()) return

        val advancementMessage = event.message() ?: return
        val advancementText = PaperComponents.plainTextSerializer().serialize(advancementMessage)
            .replaceFirst(event.player.name, "**${event.player.name.discordBoldSafe()}**")

        runBlocking {
            plugin.discordBot.bot?.send(GameChatEvent {
                content = advancementText
            })
        }
    }
}
