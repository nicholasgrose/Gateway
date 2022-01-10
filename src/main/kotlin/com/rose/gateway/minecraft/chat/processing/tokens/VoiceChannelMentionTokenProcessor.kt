package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.ResultBuilder
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.ChatTokenProcessor
import dev.kord.common.entity.ChannelType
import guru.zoroark.lixy.LixyToken
import kotlinx.coroutines.flow.toSet

class VoiceChannelMentionTokenProcessor(private val plugin: GatewayPlugin) : ChatTokenProcessor<TokenProcessingResult> {
    private val resultBuilder = ResultBuilder(plugin)

    companion object {
        const val VOICE_CHANNEL_MENTION_START_INDEX = 3
    }

    override fun regexPattern(): String {
        return "@V=[^\\s@]+"
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
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
