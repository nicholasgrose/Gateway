package com.rose.gateway.minecraft.chat

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.bot.extensions.chat.GameChatEvent
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.text.PaperComponents
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerCommandEvent

object ChatListener : Listener {
    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val messageText = PaperComponents.plainSerializer().serialize(event.message())

        runBlocking {
            val message = MinecraftMessageConverter.convertToDiscordMessage(messageText, event) ?: return@runBlocking
            DiscordBot.getBot()?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val joinMessage = event.joinMessage() ?: return
        runBlocking {
            DiscordBot.getBot()?.send(GameChatEvent {
                content = PaperComponents.plainSerializer().serialize(joinMessage)
            })
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val quitMessage = event.quitMessage() ?: return

        runBlocking {
            DiscordBot.getBot()?.send(GameChatEvent {
                content = PaperComponents.plainSerializer().serialize(quitMessage)
            })
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val deathMessage = event.deathMessage() ?: return

        runBlocking {
            DiscordBot.getBot()?.send(GameChatEvent {
                content = PaperComponents.plainSerializer().serialize(deathMessage)
            })
        }
    }

    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        val messageText = DisplayCommandProcessor.processServerCommand(event.command)

        if (messageText.isEmpty()) return

        runBlocking {
            val message = MinecraftMessageConverter.convertToDiscordMessage(messageText) ?: return@runBlocking
            DiscordBot.getBot()?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        val messageText = DisplayCommandProcessor.processPlayerCommand(event.message, event.player.name)

        if (messageText.isEmpty()) return

        runBlocking {
            val message = MinecraftMessageConverter.convertToDiscordMessage(messageText) ?: return@runBlocking
            DiscordBot.getBot()?.send(GameChatEvent(message))
        }
    }

    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        val advancementMessage = event.message() ?: return

        runBlocking {
            DiscordBot.getBot()?.send(GameChatEvent {
                content = PaperComponents.plainSerializer().serialize(advancementMessage)
            })
        }
    }
}
