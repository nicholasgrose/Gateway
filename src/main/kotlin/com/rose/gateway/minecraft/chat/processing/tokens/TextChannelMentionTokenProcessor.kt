package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.chat.processing.tokens.result.ResultBuilder
import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.TokenProcessor
import dev.kord.common.entity.ChannelType
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import kotlinx.coroutines.flow.toSet

class TextChannelMentionTokenProcessor(private val plugin: GatewayPlugin) : TokenProcessor<TokenProcessingResult> {
    companion object {
        const val TEXT_CHANNEL_MENTION_START_INDEX = 3
    }

    private val resultBuilder = ResultBuilder(plugin)

    override fun tokenType(): LixyTokenType {
        return ChatComponent.TEXT_CHANNEL_MENTION
    }

    override fun regexPattern(): String {
        return "@C=[^\\s@]+"
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val nameString = token.string.substring(TEXT_CHANNEL_MENTION_START_INDEX)

        return createTextChannelMention(nameString)
    }

    private suspend fun createTextChannelMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            for (channel in guild.channels.toSet()) {
                if (channel.type != ChannelType.GuildText) continue

                val minecraftText = "#${channel.name}"
                val discordText = "<#${channel.id}>"

                if (channel.name == nameString) return resultBuilder.mentionResult(minecraftText, discordText)
            }
        }

        return resultBuilder.errorResult("#$nameString")
    }
}
