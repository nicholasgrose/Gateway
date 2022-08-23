package com.rose.gateway.discord.bot.extensions.chat

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.chatExtensionEnabled
import com.rose.gateway.discord.bot.checks.MessageCheck
import com.rose.gateway.discord.bot.extensions.ExtensionToggle
import com.rose.gateway.discord.bot.extensions.chat.processing.DiscordMessageProcessor
import com.rose.gateway.discord.bot.message.DiscordMessageSender
import com.rose.gateway.minecraft.chat.SendMessage
import org.koin.core.component.inject

class ChatExtension : Extension() {
    companion object : ExtensionToggle {
        val config: PluginConfig by inject()

        override fun extensionName(): String {
            return "chat"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::ChatExtension
        }

        override fun isEnabled(): Boolean {
            return config.chatExtensionEnabled()
        }
    }

    override val name = extensionName()

    override suspend fun setup() {
        event {
            check(MessageCheck.notSelf, MessageCheck.isValidBotChannel)

            action {
                val message = DiscordMessageProcessor.createMessage(event)
                SendMessage.sendDiscordMessage(message)
            }
        }

        event<GameChatEvent> {
            action {
                DiscordMessageSender.sendGameChatMessage(event.message)
            }
        }
    }
}
