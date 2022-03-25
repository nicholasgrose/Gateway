package com.rose.gateway.bot.extensions.chat.processing

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.processing.TokenProcessor
import dev.kord.common.entity.Snowflake
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import org.intellij.lang.annotations.Language

class ChannelMentionTokenProcessor(private val plugin: GatewayPlugin) : TokenProcessor<Component, MessageCreateEvent> {
    override fun tokenType(): LixyTokenType {
        return DiscordChatComponent.CHANNEL_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "<#\\d+>"
    }

    override suspend fun process(token: LixyToken, additionalData: MessageCreateEvent): Component {
        val snowflakeString = token.string.substring(2 until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val channel = additionalData.getGuild()!!.getChannelOrNull(id) ?: return Component.text(token.string)

        return Component.text("#${channel.name}", plugin.configuration.primaryColor())
    }
}
