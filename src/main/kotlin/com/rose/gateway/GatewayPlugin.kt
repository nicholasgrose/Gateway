package com.rose.gateway

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.minecraft.CommandRegistry
import com.rose.gateway.minecraft.EventListeners
import com.rose.gateway.minecraft.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

/**
 * The base class and entry point for the Gateway plugin. Also provides the scope for parallelized plugin operations.
 *
 * @constructor Creates a Gateway plugin. Should not be called except when the plugin is initially loaded by the server.
 */
class GatewayPlugin : JavaPlugin(), KoinComponent, CoroutineScope {
    init {
        initializeKoin()
    }

    /**
     * Initializes Koin singletons for use throughout the Gateway plugin.
     */
    private fun initializeKoin() {
        startKoin {
            modules(
                module {
                    single { this@GatewayPlugin }
                    single { PluginConfig() }
                    single { ConfigStringMap() }
                    single { DiscordBot() }
                    single { HttpClient(CIO) }
                }
            )
        }
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
            bot.stop()
        }

        Logger.info("Gateway stopped!")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
}
