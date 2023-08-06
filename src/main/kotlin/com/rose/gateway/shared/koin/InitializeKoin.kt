package com.rose.gateway.shared.koin

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Initializes Koin singletons for use throughout the plugin
 *
 * @param plugin The plugin instance to be included as a singleton
 */
fun initializeKoin(plugin: GatewayPlugin) {
    startKoin {
        modules(
            module {
                single { plugin }
                single { PluginConfig() }
                single { ConfigStringMap() }
                single { DiscordBotController() }
                single { HttpClient(CIO) }
                single { PluginCoroutineScope() }
            },
        )
    }
}
