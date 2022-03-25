package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.ResultBuilder
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.TokenProcessor
import dev.kord.common.entity.ChannelType
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import kotlinx.coroutines.flow.toSet
import org.intellij.lang.annotations.Language

class VoiceChannelMentionTokenProcessor(private val plugin: GatewayPlugin) :
    TokenProcessor<TokenProcessingResult, Unit> {
    companion object {
        const val VOICE_CHANNEL_MENTION_START_INDEX = 3
    }

    private val resultBuilder = ResultBuilder(plugin)

    override fun tokenType(): LixyTokenType {
        return ChatComponent.VOICE_CHANNEL_MENTION
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "@V=[^\\s@]+"
    }

    override suspend fun process(token: LixyToken, additionalData: Unit): TokenProcessingResult {
        val nameString = token.string.substring(VOICE_CHANNEL_MENTION_START_INDEX)

        return createVoiceChannelMention(nameString)
    }

    private suspend fun createVoiceChannelMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildVoice) continue

                val minecraftText = "#${channel.name}"
                val discordText = "<#${channel.id}>"

                if (channel.name == nameString) return resultBuilder.mentionResult(minecraftText, discordText)
            }
        }

        return resultBuilder.errorResult("#$nameString")
    }
}
