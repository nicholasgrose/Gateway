package com.rose.gateway

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.configuration.specs.PluginSpec
import com.rose.gateway.minecraft.CommandRegistry
import com.rose.gateway.minecraft.EventListeners
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class GatewayPlugin : JavaPlugin() {
    companion object {
        const val VERSION = "1.4.1"
    }

    val startTime = Clock.System.now()
    val configuration = PluginConfiguration(this)
    var discordBot = DiscordBot(this)
    private val eventListeners = EventListeners(this)
    private val commandRegistry = CommandRegistry(this)

    override fun onEnable() {
        Logger.logInfo("Starting Gateway!")

        runBlocking {
            discordBot.start()
        }

        setConfigurationChangeActions()
        eventListeners.registerListeners(server)
        commandRegistry.registerCommands()

        Logger.logInfo("Gateway started!")
    }

    override fun onDisable() {
        Logger.logInfo("Stopping Gateway!")

        runBlocking {
            discordBot.stop()
        }

        Logger.logInfo("Gateway stopped!")
    }

    fun restartBot(): Boolean {
        runBlocking {
            discordBot.stop()
            discordBot = DiscordBot(this@GatewayPlugin)
            discordBot.start()
        }

        return discordBot.bot != null
    }

    private fun setConfigurationChangeActions() {
        PluginSpec.setConfigChangeActions(this)
    }
}
