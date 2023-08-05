package com.rose.gateway.discord.bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.loadModule
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.botToken
import com.rose.gateway.discord.bot.presence.BotPresence
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.nio.file.Path

/**
 * The Discord bot that the plugin runs
 *
 * @constructor Creates a Discord bot on standby
 */
class DiscordBot : KoinComponent {
    private val config: PluginConfig by inject()
    private val plugin: GatewayPlugin by inject()
    private val pluginScope: PluginCoroutineScope by inject()

    /**
     * The bot's context
     */
    val context = BotContext()

    /**
     * The bot's status
     */
    var botStatus = BotStatus.NOT_STARTED

    /**
     * The current instance of the bot
     */
    var bot: ExtensibleBot? = null

    init {
        // We will parallelize bot startup so that the server can continue loading while the bot starts
        pluginScope.launch {
            bot = safelyBuildBot()
        }
    }

    private var botJob: Job? = null

    /**
     * Builds a bot with error handling that returns null if building is impossible or fails
     *
     * @return The build bot or null, if building failed
     */
    private suspend fun safelyBuildBot(): ExtensibleBot? = tryKordOperation("Constructing Discord Bot") {
        if (config.notLoaded()) {
            Logger.warning("Bot construction failed because no configuration is loaded.")

            botStatus = BotStatus.STOPPED because "No valid configuration is loaded."

            null
        } else {
            Logger.info("Building Discord bot...")

            buildBot(config.botToken())
        }
    }

    /**
     * Executes a suspend function for Kord while handling any exceptions that may occur.
     *
     * @param operationName The name of the operation being performed.
     * @param operation The suspend function to be executed.
     * @return The result of the Kord operation, or null if an exception occurred.
     *
     * @suppress("TooGenericExceptionCaught")
     */
    @Suppress("TooGenericExceptionCaught")
    private suspend fun <T> tryKordOperation(operationName: String, operation: suspend () -> T?): T? = try {
        operation()
    } catch (error: java.lang.Exception) {
        val errorMessage = error.message
        val newLine = System.lineSeparator()

        val failureMessage = when (error) {
            is KordInitializationException -> "Kord failed to start:$newLine$errorMessage"
            is IOException -> "An IO exception occurred:$newLine$errorMessage"
            else -> "Unknown error occurred:$newLine$errorMessage"
        }

        botStatus = BotStatus.STOPPED because failureMessage
        Logger.error("$operationName:$newLine$failureMessage")

        null
    }

    /**
     * Builds the Discord bot on standby
     *
     * @param token The token the bot will be started with
     * @return The build bot
     */
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
                DiscordBotConstants.BOT_EXTENSIONS.map { extension -> extension.extensionConstructor() },
            )
        }
        plugins {
            // The default path of "plugins/" is problematic on a Minecraft server, so we'll remove it and
            // redirect the plugin search to "plugins/Gateway/plugins/".
            pluginPaths.clear()
            pluginPath(Path.of(plugin.dataFolder.path, "plugins"))
        }
    }

    /**
     * Gives the Kord client used by the Discord bot
     *
     * @return The Kord client or null, if no Discord bot exists
     */
    fun kordClient(): Kord? = bot?.getKoin()?.get()

    /**
     * Starts the Discord bot
     */
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

    /**
     * Unloads disabled bot extensions
     */
    private suspend fun unloadDisabledExtensions() {
        for (extension in DiscordBotConstants.BOT_EXTENSIONS) {
            if (!extension.isEnabled()) bot!!.unloadExtension(extension.extensionName())
        }
    }

    /**
     * Launches the bot in a new parallel task
     */
    private suspend fun launchConcurrentBot() {
        botJob = tryKordOperation("Running Discord Bot") {
            botStatus = BotStatus.RUNNING

            pluginScope.launch {
                bot?.start()

                botStatus = BotStatus.STOPPED
            }
        }
    }

    /**
     * Stops the Discord bot such that it can be restarted
     */
    suspend fun stop() {
        botStatus = BotStatus.STOPPING

        bot?.stop()
        botJob?.join()
    }

    /**
     * Restart the Discord bot without rebuilding it
     */
    suspend fun restart() {
        stop()
        start()
    }

    /**
     * Fully shutdown the Discord bot. It cannot be restarted unless rebuilt
     */
    suspend fun close() {
        botStatus = BotStatus.STOPPING

        bot?.close()
        botJob?.join()
    }

    /**
     * Fully rebuilds the Discord bot and starts it
     */
    suspend fun rebuild() {
        close()
        bot = safelyBuildBot()
        start()
    }
}
