package com.rose.gateway.discord.bot.presence

import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.minecraft.server.ServerInfo
import com.rose.gateway.shared.text.plurality
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Methods that modify the Discord bot's presence on Discord
 */
object BotPresence : KoinComponent {
    private val bot: DiscordBotController by inject()

    /**
     * Updates how many players are shown as currently playing in Discord
     */
    suspend fun updatePresencePlayerCount() {
        bot.discordBot.kordClient()?.editPresence {
            playing(presenceForPlayerCount())
        }
    }

    /**
     * Gives the text for the current number of players
     *
     * @return The correct text for how many players there are
     */
    fun presenceForPlayerCount(): String {
        val playerCount = ServerInfo.playerCount

        return plurality(
            playerCount,
            "Minecraft (1 Player)",
            "Minecraft ($playerCount Players)",
        )
    }
}
