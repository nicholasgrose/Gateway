package com.rose.gateway

import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.minecraft.CommandRegistry
import com.rose.gateway.minecraft.EventListeners
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import com.rose.gateway.shared.koin.initializeKoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * The base class and entry point for the Gateway plugin. Also provides the scope for parallelized plugin operations
 *
 * @constructor Creates a Gateway plugin. Should not be called except when the plugin is initially loaded by the server
 */
class GatewayPlugin : JavaPlugin(), KoinComponent {
    init {
        initializeKoin(this)
    }

    /**
     * The time that the plugin started
     */
    val startTime = Clock.System.now()

    /**
     * The classloader for this plugin
     */
    val loader = classLoader

    private val bot: DiscordBotController by inject()
    private val coroutineScope: PluginCoroutineScope by inject()

    override fun onEnable() {
        coroutineScope.launch {
            bot.start()
        }

        EventListeners.registerListeners()
        CommandRegistry.registerCommands()

        Logger.info("Gateway started!")
    }

    override fun onDisable() {
        runBlocking {
            bot.close()
            coroutineScope.cancelAndJoinContext()
        }

        Logger.info("Gateway stopped!")
    }
}
