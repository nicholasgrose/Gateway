package com.rose.gateway.minecraft.chat.processing

import com.rose.gateway.discord.text.discordBoldSafe
import com.rose.gateway.minecraft.chat.processing.tokens.RoleMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.RoleQuoteMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.TextChannelMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.TextTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.UrlTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.UserMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.UserQuoteMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.VoiceChannelMentionTokenProcessor
import com.rose.gateway.minecraft.component.join
import com.rose.gateway.shared.parsing.TextProcessor
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import io.papermc.paper.event.player.AsyncChatEvent

private val textProcessor =
    TextProcessor(
        listOf(
            UrlTokenProcessor(),
            VoiceChannelMentionTokenProcessor(),
            TextChannelMentionTokenProcessor(),
            RoleQuoteMentionTokenProcessor(),
            RoleMentionTokenProcessor(),
            UserQuoteMentionTokenProcessor(),
            UserMentionTokenProcessor(),
            TextTokenProcessor(),
        ),
    )

/**
 * Creates a Discord message for some chat message using data from its event
 *
 * @param message The message to create the Discord message from
 * @param event The event to pull the player from
 * @return The Discord message or null, if it could not be parsed
 */
suspend fun discordMessage(
    message: String,
    event: AsyncChatEvent,
): (MessageCreateBuilder.() -> Unit)? = discordMessageWithContent(message) { result ->
    val playerName = event.player.name

    event.message(result.minecraftMessage)

    "**${playerName.discordBoldSafe()} »** ${result.discordMessage}"
}

/**
 * Creates a Discord message for some chat message
 *
 * @param message The message to create the Discord message from
 * @return The Discord message or null, if it could not be parsed
 */
suspend fun discordMessage(message: String): (MessageCreateBuilder.() -> Unit)? =
    discordMessageWithContent(message) { result ->
        result.discordMessage
    }

/**
 * Parses the message and then creates a Discord message containing the content derived from the result
 *
 * @param message The message to parse
 * @param contentProvider A function that formats the content using the successful parse result
 * @receiver None
 * @return The discord message with the given content or null, if parsing failed
 */
suspend fun discordMessageWithContent(
    message: String,
    contentProvider: (MessageProcessingResult) -> String,
): (MessageCreateBuilder.() -> Unit)? {
    val result = processMessageText(message)
    val discordContent = contentProvider(result)

    return if (result.successful) {
        {
            content = discordContent
        }
    } else {
        null
    }
}

/**
 * Processes a message into a [MessageProcessingResult]
 * The result has data about success state as well as the processed message in its different forms
 *
 * @param message The message to process
 * @return The result of processing the message
 */
private suspend fun processMessageText(message: String): MessageProcessingResult {
    val messageTextParts = textProcessor.parseText(message, Unit)

    return MessageProcessingResult(
        true,
        join(
            messageTextParts.map {
                it.minecraftMessage
            },
        ),
        messageTextParts.joinToString(separator = "") { part ->
            part.discordMessage
        },
    )
}
