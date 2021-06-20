package com.rose.gateway.bot.status

import com.rose.gateway.bot.DiscordBot

object DynamicStatus {
    var playerCount = 0

    suspend fun updateStatusPlayerCount() {
        DiscordBot.getKordClient().editPresence {
            playing(statusForPlayerCount())
        }
    }

    fun statusForPlayerCount(): String {
        return when (playerCount) {
            1 -> "Minecraft ($playerCount Player)"
            else -> "Minecraft ($playerCount Players)"
        }
    }
}
