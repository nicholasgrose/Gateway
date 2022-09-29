package com.rose.gateway.discord.bot.extensions.chat

import com.kotlindiscord.kord.extensions.events.KordExEvent
import com.rose.gateway.discord.bot.DiscordBot
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * An event representing that a game chat message was created
 *
 * @property message The message to print in Discord
 * @constructor Creates a game chat event with the given message
 */
class GameChatEvent(val message: MessageCreateBuilder.() -> Unit) : KordExEvent {
    companion object : KoinComponent {
        val bot: DiscordBot by inject()

        suspend fun trigger(message: MessageCreateBuilder.() -> Unit) {
            bot.bot?.send(GameChatEvent(message))
        }
    }
}
