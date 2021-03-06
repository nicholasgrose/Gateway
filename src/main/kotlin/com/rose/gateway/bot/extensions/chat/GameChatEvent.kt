package com.rose.gateway.bot.extensions.chat

import com.kotlindiscord.kord.extensions.events.KordExEvent
import dev.kord.rest.builder.message.create.MessageCreateBuilder

class GameChatEvent(val message: MessageCreateBuilder.() -> Unit) : KordExEvent
