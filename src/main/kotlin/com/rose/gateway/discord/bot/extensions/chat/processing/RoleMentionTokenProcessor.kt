package com.rose.gateway.discord.bot.extensions.chat.processing

import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.primaryComponent
import com.rose.gateway.shared.parsing.TokenProcessor
import dev.kord.common.entity.Snowflake
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent

/**
 * Token processor that defines the role mention token and its processing
 *
 * @constructor Create a role mention token processor
 */
class RoleMentionTokenProcessor : TokenProcessor<Component, MessageCreateEvent>, KoinComponent {
    companion object {
        private const val SNOWFLAKE_START_INDEX = 3
    }

    override fun tokenType(): LixyTokenType = DiscordChatComponent.ROLE_MENTION

    @Language("RegExp")
    override fun regexPattern(): String = "<@&\\d+>"

    override suspend fun process(
        token: LixyToken,
        additionalData: MessageCreateEvent,
    ): Component {
        val snowflakeString = token.string.substring(SNOWFLAKE_START_INDEX until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val role = additionalData.getGuildOrNull()?.getRoleOrNull(id) ?: return token.string.component()

        return "@${role.name}".primaryComponent()
    }
}
