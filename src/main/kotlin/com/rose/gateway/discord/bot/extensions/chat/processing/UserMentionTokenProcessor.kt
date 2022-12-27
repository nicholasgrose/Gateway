package com.rose.gateway.discord.bot.extensions.chat.processing

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.primaryColor
import com.rose.gateway.minecraft.component.atMember
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.shared.parsing.TokenProcessor
import dev.kord.common.entity.Snowflake
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Token processor that defines the user mention token and its processing
 *
 * @constructor Create a user mention token processor
 */
class UserMentionTokenProcessor : TokenProcessor<Component, MessageCreateEvent>, KoinComponent {
    private val config: PluginConfig by inject()

    companion object {
        private const val SNOWFLAKE_START_INDEX = 2
    }

    override fun tokenType(): LixyTokenType = DiscordChatComponent.USER_MENTION

    @Language("RegExp")
    override fun regexPattern(): String = "<@\\d+>"

    override suspend fun process(token: LixyToken, additionalData: MessageCreateEvent): Component {
        val snowflakeString = token.string.substring(SNOWFLAKE_START_INDEX until token.string.length - 1)
        val id = Snowflake(snowflakeString)
        val member = additionalData.getGuild()!!.getMemberOrNull(id) ?: return token.string.component()

        return atMember(member, config.primaryColor())
    }
}
