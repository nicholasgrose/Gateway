package com.rose.gateway.bot.extensions.chat

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.checks.DefaultCheck
import com.rose.gateway.bot.checks.MessageCheck
import com.rose.gateway.bot.extensions.ToggleableExtension
import com.rose.gateway.bot.message.DiscordMessageSender
import com.rose.gateway.configuration.specs.PluginSpec
import com.rose.gateway.minecraft.chat.SendMessage
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
            val enabledSpec = PluginSpec.BotSpec.ExtensionsSpec.ChatSpec.enabled
            return plugin.configuration[enabledSpec]
        }
    }

    override val name = extensionName()
    val plugin = bot.getKoin().get<GatewayPlugin>()
    private val defaultCheck = bot.getKoin().get<DefaultCheck>()
    private val discordMessageSender = DiscordMessageSender(plugin)
    private val minecraftChatMaker = MinecraftChatMaker(plugin.configuration)

    override suspend fun setup() {
        event<MessageCreateEvent> {
            check(defaultCheck.defaultCheck, MessageCheck.notCommand, MessageCheck.notSelf)

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
