package com.rose.gateway.minecraft.whitelist

import com.rose.gateway.minecraft.server.Console
import com.rose.gateway.minecraft.server.Scheduler
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

/**
 * Provides functions that modify the Minecraft whitelist
 */
object Whitelist {
    /**
     * The set of all players in the whitelist
     */
    val players: Set<OfflinePlayer>
        get() = Bukkit.getWhitelistedPlayers()

    /**
     * Adds a player to the whitelist
     *
     * @param username The player to add
     * @return How the whitelist was modified
     */
    fun addPlayer(username: String): WhitelistState {
        val player = offlinePlayer(username)

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

    /**
     * Remove a player from the whitelist
     *
     * @param username The player to remove
     * @return How the whitelist was modified
     */
    fun removePlayer(username: String): WhitelistState {
        val player = offlinePlayer(username)

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

    /**
     * Gets an [OfflinePlayer] for the requested user
     *
     * @param username The user to get the data of
     * @return The [OfflinePlayer] for the username given or null if exists
     */
    private fun offlinePlayer(username: String): OfflinePlayer? {
        val uuid = Bukkit.getPlayerUniqueId(username) ?: return null

        return Bukkit.getOfflinePlayer(uuid)
    }
}
