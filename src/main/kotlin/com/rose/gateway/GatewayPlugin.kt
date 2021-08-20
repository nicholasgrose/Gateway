package com.rose.gateway

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.CommandRegistry
import com.rose.gateway.minecraft.EventListeners
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class GatewayPlugin : JavaPlugin() {
    companion object {
        const val VERSION = "1.3.3"
    }

    val startTime = Clock.System.now()
    val configuration = PluginConfiguration(this)
    var discordBot = DiscordBot(this)
    private val eventListeners = EventListeners(this)
    private val commandRegistry = CommandRegistry(this)

    override fun onEnable() {
        Logger.log("Starting Gateway!")

        runBlocking {
            discordBot.start()
        }

        eventListeners.registerListeners(server)
        commandRegistry.registerCommands()

        Logger.log("Gateway started!")
    }

    override fun onDisable() {
        Logger.log("Stopping Gateway!")

        runBlocking {
            discordBot.stop()
        }

        Logger.log("Gateway stopped!")
    }

    fun restartBot(): Boolean {
        if (configuration.notLoaded()) {
            return false
        }

        runBlocking {
            discordBot.stop()
            discordBot = DiscordBot(this@GatewayPlugin)
            discordBot.start()
        }

        return true
    }
}
