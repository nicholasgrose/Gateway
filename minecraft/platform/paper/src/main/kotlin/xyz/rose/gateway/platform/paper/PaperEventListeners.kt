package xyz.rose.gateway.platform.paper

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PaperEventListeners {
    companion object : Listener {
        @EventHandler(priority = EventPriority.MONITOR)
        fun onChat(event: AsyncChatEvent) {
            PaperMessageHandler.processMessage(event.message(), event.player)
        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onAdvancement(event: PlayerAdvancementDoneEvent) {

        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onJoin(event: PlayerJoinEvent) {

        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onLeave(event: PlayerQuitEvent) {

        }

        @EventHandler(priority = EventPriority.MONITOR)
        fun onDeath(event: PlayerDeathEvent) {

        }


    }
}
