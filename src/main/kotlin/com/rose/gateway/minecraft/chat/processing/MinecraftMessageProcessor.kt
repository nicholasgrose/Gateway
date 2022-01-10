package com.rose.gateway.minecraft.chat.processing

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.RoleMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.RoleQuoteMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.TextChannelMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.TextTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.UrlTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.UserMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.UserQuoteMentionTokenProcessor
import com.rose.gateway.minecraft.chat.processing.tokens.VoiceChannelMentionTokenProcessor
import com.rose.gateway.shared.discord.StringModifiers.discordBoldSafe
import com.rose.gateway.shared.processing.TextProcessor
import dev.kord.common.annotation.KordExperimental
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration

@OptIn(KordExperimental::class)
class MinecraftMessageProcessor(val plugin: GatewayPlugin) {
    private val textProcessor = TextProcessor(
        listOf(
            UserMentionTokenProcessor(plugin),
            UserQuoteMentionTokenProcessor(plugin),
            RoleMentionTokenProcessor(plugin),
            RoleQuoteMentionTokenProcessor(plugin),
            TextChannelMentionTokenProcessor(plugin),
            VoiceChannelMentionTokenProcessor(plugin),
            UrlTokenProcessor(),
            TextTokenProcessor(),
        )
    )

    suspend fun convertToDiscordMessage(
        messageText: String,
        event: AsyncChatEvent
    ): (MessageCreateBuilder.() -> Unit)? {
        val result = processMessageText(messageText)

        if (!result.successful) return null

        event.message(result.minecraftMessage)
        val playerName = event.player.name

        return {
            content = "**${playerName.discordBoldSafe()} »** ${result.discordMessage}"
        }
    }

    suspend fun convertToDiscordMessage(messageText: String): (MessageCreateBuilder.() -> Unit)? {
        val result = processMessageText(messageText)
        if (!result.successful) return null

        return {
            content = result.discordMessage
        }
    }

    private suspend fun processMessageText(messageText: String): MessageProcessingResult {
        val messageTextParts = textProcessor.parseText(messageText, Unit)

        return MessageProcessingResult(
            true,
            Component.join(
                JoinConfiguration.noSeparators(),
                messageTextParts.map {
                    it.minecraftMessage
                }
            ),
            messageTextParts.joinToString(separator = "") { part ->
                part.discordMessage
            }
        )
    }
}
