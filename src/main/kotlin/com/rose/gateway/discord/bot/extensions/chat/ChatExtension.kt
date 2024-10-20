package com.rose.gateway.discord.bot.extensions.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.chatExtensionEnabled
import com.rose.gateway.discord.bot.checks.MessageCheck
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.discord.bot.extensions.chat.processing.DiscordMessageProcessor
import com.rose.gateway.discord.bot.message.DiscordMessageSender
import com.rose.gateway.minecraft.server.Console
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.event
import org.koin.core.component.inject

/**
 * A Discord bot extension that handles the cross-chat functionality in Discord
 *
 * @constructor Create a "chat extension"
 */
class ChatExtension : Extension() {
    /**
     * The chat extension's toggle
     *
     * @constructor Create the toggle
     */
    companion object : ExtensionToggle {
        private val config: PluginConfig by inject()

        override fun extensionName(): String = "chat"

        override fun extensionConstructor(): () -> Extension = ::ChatExtension

        override fun isEnabled(): Boolean = config.chatExtensionEnabled()
    }

    override val name = extensionName()

    override suspend fun setup() {
        event {
            check(MessageCheck.notSelf, MessageCheck.isValidBotChannel)

            action {
                val message = DiscordMessageProcessor.createMessage(event)

                Console.broadcastMessage(message)
            }
        }

        event<GameChatEvent> {
            action {
                DiscordMessageSender.sendGameChatMessage(event.message)
            }
        }
    }
}
