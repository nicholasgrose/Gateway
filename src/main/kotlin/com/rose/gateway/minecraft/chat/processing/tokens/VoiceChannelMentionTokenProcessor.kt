package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.minecraft.chat.processing.tokens.result.MentionResult
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.parsing.TokenProcessor
import dev.kord.common.entity.ChannelType
import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.TokenType
import kotlinx.coroutines.flow.toSet
import org.intellij.lang.annotations.Language
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Defines and processes a voice channel mention
 *
 * @constructor Create a voice channel mention token processor
 */
class VoiceChannelMentionTokenProcessor :
    TokenProcessor<TokenProcessingResult, Unit>,
    KoinComponent {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        private const val VOICE_CHANNEL_MENTION_START_INDEX = 3
    }

    private val bot: DiscordBotController by inject()

    override fun tokenType(): TokenType = ChatComponent.VOICE_CHANNEL_MENTION

    @Language("RegExp")
    override fun regexPattern(): String = "@[vV]=[^\\s@]+"

    override suspend fun process(
        token: Token,
        additionalData: Unit,
    ): TokenProcessingResult {
        val nameString = token.string.substring(VOICE_CHANNEL_MENTION_START_INDEX)

        return createVoiceChannelMention(nameString)
    }

    private suspend fun createVoiceChannelMention(nameString: String): TokenProcessingResult {
        for (guild in bot.state.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildVoice) continue

                val minecraftText = "#${channel.name}"
                val discordText = "<#${channel.id}>"

                if (channel.name == nameString) return MentionResult.mention(minecraftText, discordText)
            }
        }

        return TokenProcessingResult.error("#$nameString")
    }
}
