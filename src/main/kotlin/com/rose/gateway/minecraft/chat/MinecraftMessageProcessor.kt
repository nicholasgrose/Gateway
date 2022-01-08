package com.rose.gateway.minecraft.chat

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.component.ComponentBuilder
import com.rose.gateway.shared.configurations.BotConfiguration.memberQueryMax
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.warningColor
import com.rose.gateway.shared.discord.StringModifiers.discordBoldSafe
import dev.kord.common.annotation.KordExperimental
import dev.kord.common.entity.ChannelType
import dev.kord.rest.builder.message.create.MessageCreateBuilder
import guru.zoroark.lixy.LixyToken
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
class MinecraftMessageProcessor(val plugin: GatewayPlugin) {
    companion object {
        const val USER_MENTION_REGEX = "@[^\\s@]+"
        const val USER_MENTION_START_INDEX = 1

        const val USER_QUOTE_MENTION_REGEX = "@\"((\\\\\")|[^\"])+\""
        const val USER_QUOTE_MENTION_START_INDEX = 2

        const val TEXT_CHANNEL_MENTION_REGEX = "@C=[^\\s@]+"
        const val TEXT_CHANNEL_MENTION_START_INDEX = 3

        const val VOICE_CHANNEL_MENTION_REGEX = "@V=[^\\s@]+"
        const val VOICE_CHANNEL_MENTION_START_INDEX = 3

        const val ROLE_MENTION_REGEX = "@R=[^\\s@]+"
        const val ROLE_MENTION_START_INDEX = 3

        const val ROLE_QUOTE_MENTION_REGEX = "@R=\"((\\\\\")|[^\"])+\""
        const val ROLE_QUOTE_MENTION_START_INDEX = 4

        const val URL_REGEX = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
        const val URL_START_INDEX = 0

        const val TEXT_REGEX = ".[^@]*"
        const val TEXT_START_INDEX = 0
    }

    data class MessageProcessingResult(
        val successful: Boolean,
        val minecraftMessage: Component,
        val discordMessage: String
    )

    data class TokenProcessingResult(
        val minecraftMessage: Component,
        val discordMessage: String
    )

    enum class ChatComponent(val pattern: String, val substringStartIndex: Int) : LixyTokenType {
        USER_MENTION(USER_MENTION_REGEX, USER_MENTION_START_INDEX),
        USER_QUOTE_MENTION(USER_QUOTE_MENTION_REGEX, USER_QUOTE_MENTION_START_INDEX),
        TEXT_CHANNEL_MENTION(TEXT_CHANNEL_MENTION_REGEX, TEXT_CHANNEL_MENTION_START_INDEX),
        VOICE_CHANNEL_MENTION(VOICE_CHANNEL_MENTION_REGEX, VOICE_CHANNEL_MENTION_START_INDEX),
        ROLE_MENTION(ROLE_MENTION_REGEX, ROLE_MENTION_START_INDEX),
        ROLE_QUOTE_MENTION(ROLE_QUOTE_MENTION_REGEX, ROLE_QUOTE_MENTION_START_INDEX),
        URL(URL_REGEX, URL_START_INDEX),
        TEXT(TEXT_REGEX, TEXT_START_INDEX)
    }

    private val chatLexer = lixy {
        state {
            matches(ChatComponent.URL.pattern) isToken ChatComponent.URL
            matches(ChatComponent.ROLE_QUOTE_MENTION.pattern) isToken ChatComponent.ROLE_QUOTE_MENTION
            matches(ChatComponent.ROLE_MENTION.pattern) isToken ChatComponent.ROLE_MENTION
            matches(ChatComponent.TEXT_CHANNEL_MENTION.pattern) isToken ChatComponent.TEXT_CHANNEL_MENTION
            matches(ChatComponent.VOICE_CHANNEL_MENTION.pattern) isToken ChatComponent.VOICE_CHANNEL_MENTION
            matches(ChatComponent.USER_QUOTE_MENTION.pattern) isToken ChatComponent.USER_QUOTE_MENTION
            matches(ChatComponent.USER_MENTION.pattern) isToken ChatComponent.USER_MENTION
            matches(ChatComponent.TEXT.pattern) isToken ChatComponent.TEXT
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
        val tokens = chatLexer.tokenize(messageText)

        val messageTextParts = tokens.map { token -> processToken(token) }

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

    private suspend fun processToken(token: LixyToken): TokenProcessingResult {
        return when (token.tokenType) {
            ChatComponent.USER_MENTION -> processTokenForComponent(
                token,
                ::createUserMention,
                ChatComponent.USER_MENTION
            )
            ChatComponent.USER_QUOTE_MENTION -> processTokenForComponent(
                token,
                ::createUserMention,
                ChatComponent.USER_QUOTE_MENTION
            )
            ChatComponent.ROLE_MENTION -> processTokenForComponent(
                token,
                ::createRoleMention,
                ChatComponent.ROLE_MENTION
            )
            ChatComponent.ROLE_QUOTE_MENTION -> processTokenForComponent(
                token,
                ::createRoleMention,
                ChatComponent.ROLE_QUOTE_MENTION
            )
            ChatComponent.TEXT_CHANNEL_MENTION -> processTokenForComponent(
                token,
                ::createTextChannelMention,
                ChatComponent.TEXT_CHANNEL_MENTION
            )
            ChatComponent.VOICE_CHANNEL_MENTION -> processTokenForComponent(
                token,
                ::createVoiceChannelMention,
                ChatComponent.VOICE_CHANNEL_MENTION
            )
            ChatComponent.URL -> processTokenForComponent(token, ::processUrl, ChatComponent.URL)
            ChatComponent.TEXT -> processTokenForComponent(token, ::processText, ChatComponent.TEXT)
            else -> emptyTokenResult()
        }
    }

    private suspend fun processTokenForComponent(
        token: LixyToken,
        processor: suspend (String) -> TokenProcessingResult,
        component: ChatComponent
    ): TokenProcessingResult {
        val tokenString = token.string

        return processor(tokenString.substring(component.substringStartIndex))
    }

    private fun emptyTokenResult(): TokenProcessingResult {
        return TokenProcessingResult(Component.empty(), "")
    }

    private suspend fun createUserMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            val members = guild.getMembers(nameString, plugin.configuration.memberQueryMax())
            val firstMember = members.firstOrNull() ?: break
            val discordText = "<@!${firstMember.id}>"

            return TokenProcessingResult(
                ComponentBuilder.atDiscordMemberComponent(firstMember, plugin.configuration.primaryColor(), plugin),
                discordText
            )
        }

        return processErrorText("@$nameString")
    }

    private suspend fun createRoleMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (role in guild.roles.toSet()) {
                val minecraftText = "@${role.name}"
                val discordText = "<@&${role.id}>"

                if (role.name == nameString) return mentionResult(minecraftText, discordText)
            }
        }

        return processErrorText("@$nameString")
    }

    private suspend fun createTextChannelMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildText) continue

                val minecraftText = "#${channel.name}"
                val discordText = "<#${channel.id}>"

                if (channel.name == nameString) return mentionResult(minecraftText, discordText)
            }
        }

        return processErrorText("#$nameString")
    }

    private suspend fun createVoiceChannelMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildVoice) continue

                val minecraftText = "#${channel.name}"
                val discordText = "<#${channel.id}>"

                if (channel.name == nameString) return mentionResult(minecraftText, discordText)
            }
        }

        return processErrorText("#$nameString")
    }

    private fun mentionResult(minecraftText: String, discordText: String): TokenProcessingResult {
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
