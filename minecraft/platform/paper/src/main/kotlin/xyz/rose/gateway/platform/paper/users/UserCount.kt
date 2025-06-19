package xyz.rose.gateway.platform.paper.users

import xyz.rose.gateway.discord.BotPresence
import xyz.rose.gateway.core.core.shared.concurrency.PluginCoroutineScope
import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Handler for events that update the Discord bot's user count
 *
 * @constructor Create a user count listener
 */
class UserCount : Listener, KoinComponent {
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    /**
     * Updates the count when a player has joined the server
     *
     * @param event The player-join event
     */
    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onJoin(event: PlayerJoinEvent) {
        pluginCoroutineScope.launch {
            BotPresence.updatePresencePlayerCount()
        }
    }

    /**
     * Updates the count when a player has quit the server
     *
     * @param event The player-quit event
     */
    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerQuitEvent) {
        pluginCoroutineScope.launch {
            BotPresence.updatePresencePlayerCount()
        }
    }

    /**
     * Updates the count when a player's connection has closed on the server
     *
     * @param event The "player connection close" event
     */
    @EventHandler
    @Suppress("UNUSED_PARAMETER")
    fun onLeave(event: PlayerConnectionCloseEvent) {
        pluginCoroutineScope.launch {
            BotPresence.updatePresencePlayerCount()
        }
    }
}
