package com.rose.gateway.minecraft.chat

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.configurations.BotConfiguration.memberQueryMax
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
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
import net.kyori.adventure.text.ComponentLike

@OptIn(KordExperimental::class)
class MinecraftMessageConverter(val plugin: GatewayPlugin) {
    data class MessageProcessingResult(
        val successful: Boolean,
        val minecraftMessage: Component,
        val discordMessage: (MessageCreateBuilder.() -> Unit)
    )

    enum class ChatComponent : LixyTokenType {
        TEXT, USER_MENTION, USER_QUOTE_MENTION, TEXT_CHANNEL_MENTION, VOICE_CHANNEL_MENTION, ROLE_MENTION, ROLE_QUOTE_MENTION
    }

    private val chatLexer = lixy {
        state {
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
        val components = mutableListOf<ComponentLike>()
        val messageTextParts = tokens.map { token ->
            val tokenString = token.string
            val mention = when (token.tokenType) {
                ChatComponent.USER_MENTION -> createUserMention(tokenString.substring(1))
                ChatComponent.USER_QUOTE_MENTION -> createUserMention(tokenString.substring(2, tokenString.length - 1))
                ChatComponent.ROLE_MENTION -> createRoleMention(tokenString.substring(3))
                ChatComponent.ROLE_QUOTE_MENTION -> createRoleMention(tokenString.substring(4, tokenString.length - 1))
                ChatComponent.TEXT_CHANNEL_MENTION -> createTextChannelMention(tokenString.substring(3))
                ChatComponent.VOICE_CHANNEL_MENTION -> createVoiceChannelMention(tokenString.substring(3))
                ChatComponent.TEXT -> null
                else -> null
            }
            return@map if (mention == null) {
                components.add(Component.text(tokenString))
                tokenString
            } else {
                components.add(Component.text(tokenString, plugin.configuration.primaryColor()))
                mention
            }
        }

        return MessageProcessingResult(true, Component.join(Component.empty(), components)) {
            content = messageTextParts.joinToString(separator = "")
        }
    }

    private suspend fun createUserMention(nameString: String): String? {
        for (guild in plugin.discordBot.botGuilds) {
            val members = guild.getMembers(nameString, plugin.configuration.memberQueryMax())
            val id = members.firstOrNull()?.id?.asString ?: return null
            return "<@!$id>"
        }

        return null
    }

    private suspend fun createRoleMention(nameString: String): String? {
        for (guild in plugin.discordBot.botGuilds) {
            for (role in guild.roles.toSet()) {
                if (role.name == nameString) return "<@&${role.id.asString}>"
            }
        }

        return null
    }

    private suspend fun createTextChannelMention(nameString: String): String? {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildText) continue
                if (channel.name == nameString) return "<#${channel.id.asString}>"
            }
        }

        return null
    }

    private suspend fun createVoiceChannelMention(nameString: String): String? {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildVoice) continue
                if (channel.name == nameString) return "<#${channel.id.asString}>"
            }
        }

        return null
    }
}
