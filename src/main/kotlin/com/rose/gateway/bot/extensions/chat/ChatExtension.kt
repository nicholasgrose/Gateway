package com.rose.gateway.bot.extensions.chat

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.bot.checks.DefaultCheck
import com.rose.gateway.bot.checks.MessageCheck
import com.rose.gateway.bot.message.MessageSender
import com.rose.gateway.minecraft.chat.SendMessage
import dev.kord.core.event.message.MessageCreateEvent

class ChatExtension : Extension() {
    override val name = "chat"

    override suspend fun setup() {
        event<MessageCreateEvent> {
            check(DefaultCheck.defaultCheck, MessageCheck.notCommand, MessageCheck.notSelf)

            action {
                val message = MinecraftChatMaker.createMessage(event)
                SendMessage.sendDiscordMessage(message)
            }
        }
        event<GameChatEvent> {
            action {
                MessageSender.sendGameChatMessage(event.message)
            }
        }
    }
}
