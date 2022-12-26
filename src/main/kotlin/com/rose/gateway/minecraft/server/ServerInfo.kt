package com.rose.gateway.minecraft.server

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Provides info about the server
 */
object ServerInfo {
    /**
     * The players currently online on the server
     */
    val onlinePlayers: Collection<Player>
        get() = Bukkit.getOnlinePlayers()

    /**
     * The number of players currently online
     */
    val playerCount: Int
        get() = onlinePlayers.size

    /**
     * A snapshot of the server's TPS
     *
     * @property oneMinute The TPS over the past minute
     * @property fiveMinute The TPS over the past five minutes
     * @property fifteenMinute The TPS over the past fifteen minutes
     * @constructor Create a TPS snapshot
     */
    public data class TPS(
        val oneMinute: Double,
        val fiveMinute: Double,
        val fifteenMinute: Double
    )

    /**
     * The server's current TPS
     */
    val tps: TPS
        get() {
            val bukkitTps = Bukkit.getTPS()

            return TPS(
                bukkitTps[0],
                bukkitTps[1],
                bukkitTps[2]
            )
        }
}
