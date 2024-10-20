package com.rose.gateway.discord.bot.extensions.chat

import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.discord.bot.DiscordBotController
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import dev.kordex.core.events.KordExEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * An event representing that a game chat message was created
 *
 * @property message The message to print in Discord
 * @constructor Creates a game chat event with the given message
 */
class GameChatEvent(
    val message: MessageCreateBuilder.() -> Unit,
) : KordExEvent {
    /**
     * Provides easy event triggers
     *
     * @constructor Construct the trigger object
     */
    companion object : KoinComponent {
        private val bot: DiscordBotController by inject()

        /**
         * Triggers a [GameChatEvent] in the [DiscordBot]
         *
         * @param message The message passed for the chat event
         * @receiver The [DiscordBot]
         */
        suspend fun trigger(message: MessageCreateBuilder.() -> Unit) {
            bot.discordBot.kordexBot
                .await()
                ?.send(GameChatEvent(message))
        }
    }
}
