package com.rose.gateway.minecraft.chat.processing.tokens.result

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.component.ComponentBuilder
import com.rose.gateway.shared.configurations.BotConfiguration.memberQueryMax
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import dev.kord.common.annotation.KordExperimental
import kotlinx.coroutines.flow.firstOrNull

class UserMentionBuilder(private val plugin: GatewayPlugin) {
    private val resultBuilder = ResultBuilder(plugin)

    @OptIn(KordExperimental::class)
    suspend fun createUserMention(nameString: String): TokenProcessingResult {
        for (guild in plugin.discordBot.botGuilds) {
            val members = guild.getMembers(nameString, plugin.configuration.memberQueryMax())
            val firstMember = members.firstOrNull() ?: break
            val discordText = "<@!${firstMember.id}>"

            return TokenProcessingResult(
                ComponentBuilder.atDiscordMemberComponent(firstMember, plugin.configuration.primaryColor(), plugin),
                discordText
            )
        }

        return resultBuilder.errorResult("@$nameString")
    }
}
