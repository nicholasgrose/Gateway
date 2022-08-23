package com.rose.gateway.discord.bot.extensions.chat

import com.kotlindiscord.kord.extensions.events.KordExEvent
import dev.kord.rest.builder.message.create.MessageCreateBuilder

/**
 * An event representing that a game chat message was created.
 *
 * @property message The message to print in Discord.
 * @constructor Creates a game chat event with the given message.
 */
class GameChatEvent(val message: MessageCreateBuilder.() -> Unit) : KordExEvent
