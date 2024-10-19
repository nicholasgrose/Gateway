package com.rose.gateway.minecraft.chat.processing.tokens.result

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.primaryColor
import com.rose.gateway.discord.bot.DiscordBotConstants
import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.minecraft.component.atMember
import com.rose.gateway.minecraft.component.primaryComponent
import dev.kord.common.annotation.KordExperimental
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toSet
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides functions that build [TokenProcessingResult]s for mentions
 */
object MentionResult : KoinComponent {
    private val bot: DiscordBotController by inject()
    private val config: PluginConfig by inject()

    /**
     * Creates a [TokenProcessingResult] representing a mention in Discord
     *
     * @param minecraftText The text as it will appear in Minecraft
     * @param discordText The text as it will appear in Discord
     * @return The [TokenProcessingResult] for the mention
     */
    fun mention(
        minecraftText: String,
        discordText: String,
    ): TokenProcessingResult = TokenProcessingResult(
        minecraftText.primaryComponent(),
        discordText,
    )

    /**
     * Creates a [TokenProcessingResult] representing a role mention in Discord
     *
     * @param nameString The name of the role to mention
     * @return The [TokenProcessingResult] for the role mention
     */
    suspend fun role(nameString: String): TokenProcessingResult {
        for (guild in bot.state.botGuilds) {
            for (role in guild.roles.toSet()) {
                val minecraftText = "@${role.name}"
                val discordText = "<@&${role.id}>"

                if (role.name == nameString) return mention(minecraftText, discordText)
            }
        }

        return TokenProcessingResult.error("@$nameString")
    }

    /**
     * Creates a [TokenProcessingResult] representing a user mention in Discord
     *
     * @param nameString The name of the user to mention
     * @return The [TokenProcessingResult] for the user mention
     */
    @OptIn(KordExperimental::class)
    suspend fun userMention(nameString: String): TokenProcessingResult {
        for (guild in bot.state.botGuilds) {
            val members = guild.getMembers(nameString, DiscordBotConstants.MEMBER_QUERY_MAX)
            val firstMember = members.firstOrNull() ?: break
            val discordText = "<@!${firstMember.id}>"

            return TokenProcessingResult(
                atMember(firstMember, config.primaryColor()),
                discordText,
            )
        }

        return TokenProcessingResult.error("@$nameString")
    }
}
