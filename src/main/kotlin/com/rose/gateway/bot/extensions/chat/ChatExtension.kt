package com.rose.gateway.bot.extensions.chat

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.checks.MessageCheck
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.bot.extensions.chat.processing.DiscordMessageProcessor
import com.rose.gateway.bot.message.DiscordMessageSender
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.chat.SendMessage
import com.rose.gateway.shared.configurations.BotConfiguration.chatExtensionEnabled
import dev.kord.core.event.message.MessageCreateEvent
import org.koin.core.component.inject

class ChatExtension : Extension() {
    companion object : ToggleableExtension {
        val config: PluginConfiguration by inject()

        override fun extensionName(): String {
            return "chat"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::ChatExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            return config.chatExtensionEnabled()
        }
    }

    override val name = extensionName()
    private val messageCheck = MessageCheck()
    private val discordMessageSender = DiscordMessageSender()
    private val discordChatProcessor = DiscordMessageProcessor()

    override suspend fun setup() {
        event<MessageCreateEvent> {
            check(messageCheck.notSelf, messageCheck.isConfiguredBotChannel)

            action {
                val message = discordChatProcessor.createMessage(event)
                SendMessage.sendDiscordMessage(message)
            }
        }

        event<GameChatEvent> {
            action {
                discordMessageSender.sendGameChatMessage(event.message)
            }
        }
    }
}
