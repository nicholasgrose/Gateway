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

class MinecraftChatMaker(private val pluginConfiguration: PluginConfiguration) {
    suspend fun createMessage(event: MessageCreateEvent): Component {
        return Component.join(
            Component.empty(),
            generateNameBlock(event),
            generateMessagePrefixBlock(event),
            generateMessageBlock(event)
        )
    }

    private fun generateNameBlock(event: MessageCreateEvent): ComponentLike {
        val username = event.member!!.displayName

        return Component.join(
            Component.empty(),
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
        USER_MENTION, ROLE_MENTION, CHANNEL_MENTION, TEXT
    }

    private val discordMessageLexer = lixy {
        state {
            matches("<@!\\d+>") isToken DisplayComponent.USER_MENTION
            matches("<@&\\d+>") isToken DisplayComponent.ROLE_MENTION
            matches("<#\\d+>") isToken DisplayComponent.CHANNEL_MENTION
            matches(".[^<]*") isToken DisplayComponent.TEXT
        }
    }

    private val tokenToComponentTransformer = mapOf(
        DisplayComponent.USER_MENTION to ::processUserMention,
        DisplayComponent.ROLE_MENTION to ::processRoleMention,
        DisplayComponent.CHANNEL_MENTION to ::processChannelMention,
        DisplayComponent.TEXT to ::processText
    )

    private suspend fun generateMessageBlock(event: MessageCreateEvent): ComponentLike {
        val messageComponents = discordMessageLexer.tokenize(event.message.content)

        return Component.join(
            Component.empty(),
            messageComponents.map { token -> processToken(token, event) }
        )
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun processToken(token: LixyToken, event: MessageCreateEvent): ComponentLike {
        return tokenToComponentTransformer[token.tokenType]!!(token, event)
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

    @Suppress("RedundantSuspendModifier", "UNUSED_PARAMETER")
    private suspend fun processText(token: LixyToken, event: MessageCreateEvent): ComponentLike {
        return Component.text(token.string)
    }
}
