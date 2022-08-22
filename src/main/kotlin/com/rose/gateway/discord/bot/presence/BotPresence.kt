package com.rose.gateway.discord.bot.presence

import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.minecraft.server.ServerInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object BotPresence : KoinComponent {
    val bot: DiscordBot by inject()

    suspend fun updatePresencePlayerCount() {
        bot.kordClient()?.editPresence {
            playing(presenceForPlayerCount())
        }
    }

    fun presenceForPlayerCount(): String {
        return when (val playerCount = ServerInfo.playerCount()) {
            1 -> "Minecraft ($playerCount Player)"
            else -> "Minecraft ($playerCount Players)"
        }
    }
}
