package com.rose.gateway.bot.extensions.chat

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.secondaryColor
import dev.kord.common.entity.Snowflake
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

class MinecraftChatMaker(private val pluginConfiguration: PluginConfiguration) {
    suspend fun createMessage(event: MessageCreateEvent): Component {
        return Component.join(
            JoinConfiguration.noSeparators(),
            generateNameBlock(event),
            generateMessagePrefixBlock(event),
            generateMessageBlock(event)
        )
    }

    private fun generateNameBlock(event: MessageCreateEvent): ComponentLike {
        val username = event.member!!.displayName

        return Component.join(
            JoinConfiguration.noSeparators(),
            Component.text("<"),
            Component.text(username, pluginConfiguration.secondaryColor()),
            Component.text("> ")
        )
    }

    private suspend fun generateMessagePrefixBlock(event: MessageCreateEvent): ComponentLike {
        val referencedMessage = event.message.referencedMessage ?: return Component.empty()
        val referencedAuthor = referencedMessage.author?.id ?: return Component.empty()
        val guild = event.getGuild() ?: return Component.empty()
        val name = guild.getMemberOrNull(referencedAuthor)?.displayName ?: return Component.empty()

        return Component.text("(Replying to @$name) ", pluginConfiguration.primaryColor())
    }

    enum class DisplayComponent : LixyTokenType {
        USER_MENTION, ROLE_MENTION, CHANNEL_MENTION, URL, TEXT
    }

    private val discordMessageLexer = lixy {
        state {
            matches("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]") isToken DisplayComponent.URL
            matches("<@!\\d+>") isToken DisplayComponent.USER_MENTION
            matches("<@&\\d+>") isToken DisplayComponent.ROLE_MENTION
            matches("<#\\d+>") isToken DisplayComponent.CHANNEL_MENTION
            matches(".[^<]*") isToken DisplayComponent.TEXT
        }
    }

    private suspend fun generateMessageBlock(event: MessageCreateEvent): ComponentLike {
        val messageComponents = discordMessageLexer.tokenize(event.message.content)

        return Component.join(
            JoinConfiguration.noSeparators(),
            messageComponents.map { token -> processToken(token, event) }
        )
    }

    private suspend fun processToken(token: LixyToken, event: MessageCreateEvent): ComponentLike {
        return when (token.tokenType) {
            DisplayComponent.USER_MENTION -> processUserMention(token, event)
            DisplayComponent.ROLE_MENTION -> processRoleMention(token, event)
            DisplayComponent.CHANNEL_MENTION -> processChannelMention(token, event)
            DisplayComponent.URL -> processUrl(token)
            DisplayComponent.TEXT -> processText(token)
            else -> Component.empty()
        }
    }

    private suspend fun processUserMention(token: LixyToken, event: MessageCreateEvent): ComponentLike {
        val snowflakeString = token.string.substring(3 until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val member = event.getGuild()!!.getMemberOrNull(id) ?: return Component.text(token.string)

        return Component.text("@${member.displayName}", pluginConfiguration.primaryColor())
    }

    private suspend fun processRoleMention(token: LixyToken, event: MessageCreateEvent): ComponentLike {
        val snowflakeString = token.string.substring(3 until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val role = event.getGuild()!!.getRoleOrNull(id) ?: return Component.text(token.string)

        return Component.text("@${role.name}", pluginConfiguration.primaryColor())
    }

    private suspend fun processChannelMention(token: LixyToken, event: MessageCreateEvent): ComponentLike {
        val snowflakeString = token.string.substring(2 until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val channel = event.getGuild()!!.getChannelOrNull(id) ?: return Component.text(token.string)

        return Component.text("#${channel.name}", pluginConfiguration.primaryColor())
    }

    private fun processUrl(token: LixyToken): ComponentLike {
        val url = token.string

        return Component.text(url)
            .decorate(TextDecoration.UNDERLINED)
            .hoverEvent(HoverEvent.showText(Component.text("Click to open url")))
            .clickEvent(ClickEvent.openUrl(url))
    }

    private fun processText(token: LixyToken): ComponentLike {
        return Component.text(token.string)
    }
}
