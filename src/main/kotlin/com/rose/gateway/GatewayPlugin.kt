package com.rose.gateway

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.ConfigurationStringMap
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.CommandRegistry
import com.rose.gateway.minecraft.EventListeners
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * The base class and entry point for the Gateway plugin.
 *
 * @constructor Creates Gateway plugin. Should not be called except when the plugin is initially loaded by the server.
 */
class GatewayPlugin : JavaPlugin(), KoinComponent {
    val startTime = Clock.System.now()
    val loader = classLoader
    private val bot: DiscordBot by inject()

    override fun onEnable() {
        Logger.info("Starting Gateway!")
        initializeKoin()

        runBlocking {
            bot.start()
        }

        EventListeners.registerListeners()
        CommandRegistry.registerCommands()

        Logger.info("Gateway started!")
    }

    /**
     * Initializes Koin singletons for use throughout the Gateway plugin.
     */
    private fun initializeKoin() {
        startKoin {
            modules(
                module {
                    single { this@GatewayPlugin }
                    single { PluginConfiguration() }
                    single { ConfigurationStringMap() }
                    single { DiscordBot() }
                    single { HttpClient(CIO) }
                }
            )
        }
    }

    override fun onDisable() {
        Logger.info("Stopping Gateway!")

        runBlocking {
            bot.stop()
        }

        Logger.info("Gateway stopped!")
    }
}
