package com.rose.gateway.minecraft.chat.processing.tokens.result

import com.rose.gateway.bot.DiscordBot
import kotlinx.coroutines.flow.toSet
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RoleMentionBuilder : KoinComponent {
    private val bot: DiscordBot by inject()

    private val resultBuilder = ResultBuilder()

    suspend fun createRoleMention(nameString: String): TokenProcessingResult {
        for (guild in bot.botGuilds) {
            for (role in guild.roles.toSet()) {
                val minecraftText = "@${role.name}"
                val discordText = "<@&${role.id}>"

                if (role.name == nameString) return resultBuilder.mentionResult(minecraftText, discordText)
            }
        }

        return resultBuilder.errorResult("@$nameString")
    }
}
