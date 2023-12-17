package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.discord.bot.extensions.chat.GameChatEvent
import com.rose.gateway.discord.text.discordBoldSafe
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Listener that posts major player actions in Discord
 *
 * @constructor Create an action listener
 */
class ActionListener : Listener, KoinComponent {
    private val config: PluginConfig by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    /**
     * Posts a player's death in Discord
     *
     * @param event The player death event
     */
    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            val deathMessage = event.deathMessage() ?: return@launchIfChatExtensionEnabled
            val plainTextMessage =
                PlainTextComponentSerializer.plainText().serialize(deathMessage)
                    .replaceFirst(event.player.name, "**${event.player.name.discordBoldSafe()}**")

            GameChatEvent.trigger {
                content = plainTextMessage
            }
        }
    }

    /**
     * Posts a player's advancement in Discord
     *
     * @param event The player advancement completion event
     */
    @EventHandler
    fun onPlayerAdvancement(event: PlayerAdvancementDoneEvent) {
        pluginCoroutineScope.launchIfChatExtensionEnabled(config) {
            val advancementMessage = event.message() ?: return@launchIfChatExtensionEnabled
            val advancementText =
                PlainTextComponentSerializer.plainText().serialize(advancementMessage)
                    .replaceFirst(event.player.name, "**${event.player.name.discordBoldSafe()}**")

            GameChatEvent.trigger {
                content = advancementText
            }
        }
    }
}
