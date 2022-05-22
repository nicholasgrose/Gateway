package com.rose.gateway.minecraft.whitelist

import com.rose.gateway.minecraft.server.Console
import com.rose.gateway.minecraft.server.Scheduler
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

object Whitelist {
    fun addToWhitelist(username: String): WhitelistState {
        val player = getOfflinePlayer(username)

        return when {
            player == null -> WhitelistState.STATE_INVALID
            player.isWhitelisted -> WhitelistState.STATE_SUSTAINED
            else -> {
                Scheduler.runTask {
                    Console.runCommand("whitelist add $username")
                }
                WhitelistState.STATE_MODIFIED
            }
        }
    }

    fun removeFromWhitelist(username: String): WhitelistState {
        val player = getOfflinePlayer(username)

        return when {
            player == null -> WhitelistState.STATE_INVALID
            player.isWhitelisted -> WhitelistState.STATE_SUSTAINED
            else -> {
                Scheduler.runTask {
                    player.player?.kick(Component.text("Removed from whitelist."))
                    Console.runCommand("whitelist remove $username")
                }
                WhitelistState.STATE_MODIFIED
            }
        }
    }

    fun whitelistedPlayersAsString(): String {
        val whitelistedPlayers = Bukkit.getWhitelistedPlayers()
        return whitelistedPlayers.map { it.name }.joinToString(separator = ", ")
    }

    private fun getOfflinePlayer(username: String): OfflinePlayer? {
        val uuid = Bukkit.getPlayerUniqueId(username) ?: return null
        return Bukkit.getOfflinePlayer(uuid)
    }
}
