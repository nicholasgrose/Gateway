package com.rose.gateway.discord.bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.loadModule
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.botToken
import com.rose.gateway.discord.bot.presence.BotPresence
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DiscordBot : KoinComponent {
    private val config: PluginConfig by inject()
    private val plugin: GatewayPlugin by inject()
    private val pluginScope: PluginCoroutineScope by inject()

    val context = BotContext()
    var botStatus = BotStatus.NOT_STARTED
    var bot = runBlocking {
        safelyBuildBot()
    }

    private var botJob: Job? = null

    private suspend fun safelyBuildBot(): ExtensibleBot? = try {
        if (config.notLoaded()) {
            Logger.warning("Bot construction failed because no configuration is loaded.")

            botStatus = BotStatus.STOPPED because "No valid configuration is loaded."

            null
        } else {
            Logger.info("Building Discord bot...")

            buildBot(config.botToken())
        }
    } catch (e: KordInitializationException) {
        Logger.warning("Bot construction failed (${e.localizedMessage})")

        botStatus = BotStatus.STOPPED because e.localizedMessage

        null
    }

    private suspend fun buildBot(token: String): ExtensibleBot = ExtensibleBot(token) {
        hooks {
            kordShutdownHook = false

            afterKoinSetup {
                loadModule {
                    single { plugin }
                    single { config }
                }
            }
        }
        presence {
            since = plugin.startTime
            playing(BotPresence.presenceForPlayerCount())
        }
        applicationCommands {
            enabled = true
        }
        extensions {
            extensions.addAll(
                DiscordBotConstants.BOT_EXTENSIONS.map { extension -> extension.extensionConstructor() }
            )
        }
    }

    fun kordClient(): Kord? = bot?.getKoin()?.get()

    suspend fun start() {
        Logger.info("Starting Discord bot...")

        if (bot == null) {
            Logger.warning("Could not start because no valid bot exists. Check bot status for error.")

            return
        }

        botStatus = BotStatus.STARTING

        unloadDisabledExtensions()
        context.fillBotChannels()
        launchConcurrentBot()

        Logger.info("Discord bot ready!")
    }

    private suspend fun unloadDisabledExtensions() {
        for (extension in DiscordBotConstants.BOT_EXTENSIONS) {
            if (!extension.isEnabled(plugin)) bot!!.unloadExtension(extension.extensionName())
        }
    }

    private suspend fun launchConcurrentBot() {
        botJob = try {
            botStatus = BotStatus.RUNNING

            pluginScope.launch {
                bot?.start()

                botStatus = BotStatus.STOPPED
            }
        } catch (error: KordInitializationException) {
            val message = "An error occurred while running bot: ${error.message}"

            botStatus = BotStatus.STOPPED because message
            Logger.warning("Could not start Discord bot. Check status for info.")

            null
        }
    }

    private suspend fun stop() {
        botStatus = BotStatus.STOPPING

        bot?.stop()
        botJob?.join()
    }

    suspend fun restart() {
        stop()
        start()
    }

    suspend fun close() {
        botStatus = BotStatus.STOPPING

        bot?.close()
        botJob?.join()
    }

    suspend fun rebuild() {
        close()
        bot = safelyBuildBot()
        start()
    }
}
