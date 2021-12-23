package com.rose.gateway.bot.extensions.chat

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.checks.MessageCheck
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.bot.message.DiscordMessageSender
import com.rose.gateway.minecraft.chat.SendMessage
import com.rose.gateway.shared.configurations.BotConfiguration.chatExtensionEnabled
import dev.kord.core.event.message.MessageCreateEvent

class ChatExtension : Extension() {
    companion object : ToggleableExtension {
        override fun extensionName(): String {
            return "chat"
        }

        override fun extensionConstructor(): () -> Extension {
            return ::ChatExtension
        }

        override fun isEnabled(plugin: GatewayPlugin): Boolean {
            return plugin.configuration.chatExtensionEnabled()
        }
    }

    override val name = extensionName()
    val plugin = bot.getKoin().get<GatewayPlugin>()
    private val messageCheck = MessageCheck(plugin)
    private val discordMessageSender = DiscordMessageSender(plugin)
    private val minecraftChatMaker = MinecraftChatMaker(plugin)

    override suspend fun setup() {
        event<MessageCreateEvent> {
            check(messageCheck.notSelf, messageCheck.isConfiguredBotChannel)

            action {
                val message = minecraftChatMaker.createMessage(event)
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
