package com.rose.gateway.bot.presence

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.server.ServerInfo

class DynamicPresence(val plugin: GatewayPlugin) {
    suspend fun updatePresencePlayerCount() {
        plugin.discordBot.kordClient?.editPresence {
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
