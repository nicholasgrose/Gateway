package com.rose.gateway.bot.presence

import com.rose.gateway.bot.DiscordBot

object DynamicPresence {
    var playerCount = 0

    suspend fun updatePresencePlayerCount() {
        DiscordBot.getKordClient()?.editPresence {
            playing(presenceForPlayerCount())
        }
    }

    fun presenceForPlayerCount(): String {
        return when (playerCount) {
            1 -> "Minecraft ($playerCount Player)"
            else -> "Minecraft ($playerCount Players)"
        }
    }
}
