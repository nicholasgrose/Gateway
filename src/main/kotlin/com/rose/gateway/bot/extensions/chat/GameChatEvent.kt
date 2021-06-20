package com.rose.gateway.bot.extensions.chat

import com.kotlindiscord.kord.extensions.events.ExtensionEvent
import com.rose.gateway.bot.DiscordBot
import dev.kord.rest.builder.message.MessageCreateBuilder

class GameChatEvent(val message: MessageCreateBuilder.() -> Unit) : ExtensionEvent {
    override val bot by lazy { DiscordBot.getBot() }
}
