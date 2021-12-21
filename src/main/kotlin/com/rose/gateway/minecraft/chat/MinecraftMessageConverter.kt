package com.rose.gateway.minecraft.chat

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.configurations.BotConfiguration.memberQueryMax
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.warningColor
import dev.kord.common.annotation.KordExperimental
import dev.kord.common.entity.ChannelType
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

@OptIn(KordExperimental::class)
class MinecraftMessageConverter(val plugin: GatewayPlugin) {
    data class MessageProcessingResult(
        val successful: Boolean,
        val minecraftMessage: Component,
        val discordMessage: (MessageCreateBuilder.() -> Unit)
    )

    data class TokenProcessingResult(
        val minecraftMessage: Component,
        val discordMessage: String
    )

    enum class ChatComponent : LixyTokenType {
        USER_MENTION, USER_QUOTE_MENTION, TEXT_CHANNEL_MENTION, VOICE_CHANNEL_MENTION, ROLE_MENTION, ROLE_QUOTE_MENTION, URL, TEXT
    }

    private val chatLexer = lixy {
        state {
            matches("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]") isToken ChatComponent.URL
            matches("@R=\"((\\\\\")|[^\"])+\"") isToken ChatComponent.ROLE_QUOTE_MENTION
            matches("@R=[^\\s@]+") isToken ChatComponent.ROLE_MENTION
            matches("@C=[^\\s@]+") isToken ChatComponent.TEXT_CHANNEL_MENTION
            matches("@V=[^\\s@]+") isToken ChatComponent.VOICE_CHANNEL_MENTION
            matches("@\"((\\\\\")|[^\"])+\"") isToken ChatComponent.USER_QUOTE_MENTION
            matches("@[^\\s@]+") isToken ChatComponent.USER_MENTION
            matches(".[^@]*") isToken ChatComponent.TEXT
        }
    }

    suspend fun convertToDiscordMessage(
        messageText: String,
        event: AsyncChatEvent
    ): (MessageCreateBuilder.() -> Unit)? {
        val result = processMessageText(messageText)

        if (!result.successful) return null

        event.message(result.minecraftMessage)
        val playerName = event.player.name

        return {
            result.discordMessage.invoke(this)
            content = "<$playerName> $content"
        }
    }

    suspend fun convertToDiscordMessage(messageText: String): (MessageCreateBuilder.() -> Unit)? {
        val result = processMessageText(messageText)
        if (!result.successful) return null

        return result.discordMessage
    }

    private suspend fun processMessageText(messageText: String): MessageProcessingResult {
        val tokens = chatLexer.tokenize(messageText)

        val messageTextParts = tokens.map { token ->
            val tokenString = token.string
            val tokenProcessingResult = when (token.tokenType) {
                ChatComponent.USER_MENTION -> createUserMention(tokenString.substring(1))
                ChatComponent.USER_QUOTE_MENTION -> createUserMention(tokenString.substring(2, tokenString.length - 1))
                ChatComponent.ROLE_MENTION -> createRoleMention(tokenString.substring(3))
                ChatComponent.ROLE_QUOTE_MENTION -> createRoleMention(tokenString.substring(4, tokenString.length - 1))
                ChatComponent.TEXT_CHANNEL_MENTION -> createTextChannelMention(tokenString.substring(3))
                ChatComponent.VOICE_CHANNEL_MENTION -> createVoiceChannelMention(tokenString.substring(3))
                ChatComponent.URL -> processUrl(tokenString)
                ChatComponent.TEXT -> processText(tokenString)
                else -> emptyTokenResult()
            }

            tokenProcessingResult
        }

        return MessageProcessingResult(
            true,
            Component.join(JoinConfiguration.noSeparators(),
                messageTextParts.map {
                    it.minecraftMessage
                }
            )) {
            content = messageTextParts.joinToString(separator = "") {
                it.discordMessage
            }
        }
    }

    private fun emptyTokenResult(): TokenProcessingResult {
        return TokenProcessingResult(Component.empty(), "")
    }

    private suspend fun createUserMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            val members = guild.getMembers(nameString, plugin.configuration.memberQueryMax())
            val firstMember = members.firstOrNull() ?: return processErrorText("@$nameString")
            val id = firstMember.id.asString
            val minecraftText = "@${firstMember.displayName}"
            val discordText = "<@!$id>"

            return mentionComponent(minecraftText, discordText)
        }

        return processErrorText("@$nameString")
    }

    private suspend fun createRoleMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (role in guild.roles.toSet()) {
                val minecraftText = "@${role.name}"
                val discordText = "<@&${role.id.asString}>"

                if (role.name == nameString) return mentionComponent(minecraftText, discordText)
            }
        }

        return processErrorText("@$nameString")
    }

    private suspend fun createTextChannelMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildText) continue

                val minecraftText = "#${channel.name}"
                val discordText = "<#${channel.id.asString}>"

                if (channel.name == nameString) return mentionComponent(minecraftText, discordText)
            }
        }

        return processErrorText("#$nameString")
    }

    private suspend fun createVoiceChannelMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildVoice) continue

                val minecraftText = "#${channel.name}"
                val discordText = "<#${channel.id.asString}>"

                if (channel.name == nameString) return mentionComponent(minecraftText, discordText)
            }
        }

        return processErrorText("#$nameString")
    }

    private fun mentionComponent(minecraftText: String, discordText: String): TokenProcessingResult {
        return TokenProcessingResult(
            Component.text(minecraftText)
                .color(plugin.configuration.primaryColor()),
            discordText
        )
    }


    private fun processUrl(url: String): TokenProcessingResult {
        val component = Component.text(url)
            .decorate(TextDecoration.UNDERLINED)
            .hoverEvent(HoverEvent.showText(Component.text("Click to open url")))
            .clickEvent(ClickEvent.openUrl(url))

        return TokenProcessingResult(component, url)
    }

    private fun processErrorText(text: String): TokenProcessingResult {
        return TokenProcessingResult(
            Component.text(text)
                .color(plugin.configuration.warningColor()),
            text
        )
    }

    private fun processText(text: String): TokenProcessingResult {
        return TokenProcessingResult(Component.text(text), text)
    }
}
