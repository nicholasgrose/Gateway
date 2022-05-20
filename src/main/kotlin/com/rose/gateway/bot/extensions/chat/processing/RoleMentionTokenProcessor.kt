package com.rose.gateway.bot.extensions.chat.processing

import com.rose.gateway.configuration.PluginConfiguration
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

class RoleMentionTokenProcessor : TokenProcessor<Component, MessageCreateEvent>, KoinComponent {
    private val config: PluginConfiguration by inject()

    companion object {
        const val SNOWFLAKE_START_INDEX = 3
    }

    override fun tokenType(): LixyTokenType {
        return DiscordChatComponent.ROLE_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "<@&\\d+>"
    }

    override suspend fun process(token: LixyToken, additionalData: MessageCreateEvent): Component {
        val snowflakeString = token.string.substring(SNOWFLAKE_START_INDEX until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val role = additionalData.getGuild()!!.getRoleOrNull(id) ?: return Component.text(token.string)

        return Component.text("@${role.name}", config.primaryColor())
    }
}
