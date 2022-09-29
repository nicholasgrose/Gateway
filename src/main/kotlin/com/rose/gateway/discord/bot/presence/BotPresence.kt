package com.rose.gateway.discord.bot.presence

import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.minecraft.server.ServerInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Methods that modify the Discord bot's presence on Discord
 */
object BotPresence : KoinComponent {
    private val bot: DiscordBot by inject()

    /**
     * Updates how many players are shown as currently playing in Discord
     */
    suspend fun updatePresencePlayerCount() {
        bot.kordClient()?.editPresence {
            playing(presenceForPlayerCount())
        }
    }

    /**
     * Gives the text for the current number of players
     *
     * @return The correct text for how many players there are
     */
    fun presenceForPlayerCount(): String {
        return when (val playerCount = ServerInfo.playerCount) {
            1 -> "Minecraft ($playerCount Player)"
            else -> "Minecraft ($playerCount Players)"
        }
    }
}
