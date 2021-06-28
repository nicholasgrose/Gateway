package com.rose.gateway

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.minecraft.CommandRegistry
import com.rose.gateway.minecraft.EventListeners
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class GatewayPlugin : JavaPlugin() {
    companion object {
        const val VERSION = "1.3.3"
        lateinit var plugin: GatewayPlugin
    }

    var discordBot = DiscordBot()

    override fun onEnable() {
        Logger.log("Starting Gateway!")

        plugin = this
        if (!Configurator.ensureProperConfiguration()) return
        runBlocking {
            discordBot.start()
        }
        EventListeners.registerListeners(server)
        CommandRegistry.registerCommands()

        Logger.log("Gateway started!")
    }

    override fun onDisable() {
        Logger.log("Stopping Gateway!")

        runBlocking {
            discordBot.stop()
        }

        Logger.log("Gateway stopped!")
    }

    fun restartBot() {
        runBlocking {
            discordBot.stop()
            discordBot = DiscordBot()
            discordBot.start()
        }
    }
}
