package com.rose.gateway.bot.extensions.chat.processing

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.shared.component.ComponentBuilder
import com.rose.gateway.shared.configurations.primaryColor
import com.rose.gateway.shared.processing.TokenProcessor
import dev.kord.common.entity.Snowflake
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserMentionTokenProcessor : TokenProcessor<Component, MessageCreateEvent>, KoinComponent {
    private val config: PluginConfiguration by inject()

    companion object {
        const val SNOWFLAKE_START_INDEX = 2
    }

    override fun tokenType(): LixyTokenType {
        return DiscordChatComponent.USER_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "<@\\d+>"
    }

    override suspend fun process(token: LixyToken, additionalData: MessageCreateEvent): Component {
        val snowflakeString = token.string.substring(SNOWFLAKE_START_INDEX until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val member = additionalData.getGuild()!!.getMemberOrNull(id) ?: return Component.text(token.string)

        return ComponentBuilder.atDiscordMemberComponent(member, config.primaryColor())
    }
}
