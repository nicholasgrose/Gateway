package com.rose.gateway

import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.minecraft.CommandRegistry
import com.rose.gateway.minecraft.EventListeners
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.koin.initializeKoin
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * The base class and entry point for the Gateway plugin. Also provides the scope for parallelized plugin operations.
 *
 * @constructor Creates a Gateway plugin. Should not be called except when the plugin is initially loaded by the server.
 */
class GatewayPlugin : JavaPlugin(), KoinComponent {
    init {
        initializeKoin(this)
    }

    val startTime = Clock.System.now()
    val loader = classLoader
    private val bot: DiscordBot by inject()

    override fun onEnable() {
        Logger.info("Starting Gateway!")

        runBlocking {
            bot.start()
        }

        EventListeners.registerListeners()
        CommandRegistry.registerCommands()

        Logger.info("Gateway started!")
    }

    override fun onDisable() {
        Logger.info("Stopping Gateway!")

        runBlocking {
            bot.close()
        }

        Logger.info("Gateway stopped!")
    }
}
