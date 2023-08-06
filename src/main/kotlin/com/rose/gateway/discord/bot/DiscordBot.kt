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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.nio.file.Files
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
     * The current instance of the KordEx [ExtensibleBot]]
     */
    val kordexBot: Deferred<ExtensibleBot?> = pluginScope.async {
        safelyBuildBot().getOrNull()
    }

    /**
     * Builds a bot with error handling that returns null if building is impossible or fails
     *
     * @return The build bot or null, if building failed
     */
    suspend fun safelyBuildBot(): Result<ExtensibleBot> = tryKordOperation("Constructing Discord Bot") {
        if (config.notLoaded()) {
            Logger.warning("Bot construction failed because no configuration is loaded.")

            Result.failure(Exception("No valid configuration is loaded."))
        } else {
            Logger.info("Building Discord bot...")

            Result.success(buildBot(config.botToken()))
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
    suspend fun <T> tryKordOperation(operationName: String, operation: suspend () -> Result<T>): Result<T> =
        try {
            operation()
        } catch (error: java.lang.Exception) {
            val errorMessage = error.message
            val newLine = System.lineSeparator()

            val failureMessage = when (error) {
                is KordInitializationException -> "Kord failed to start:$newLine$errorMessage"
                is IOException -> "An IO exception occurred:$newLine$errorMessage"
                else -> "Unknown error occurred:$newLine$errorMessage"
            }

            Logger.error("$operationName:$newLine$failureMessage")

            Result.failure(error)
        }

    /**
     * Builds the Discord bot on standby
     *
     * @param token The token the bot will be started with
     * @return The build bot
     */
    private suspend fun buildBot(token: String): ExtensibleBot {
        val bot = ExtensibleBot(token) {
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
                val correctPluginPath = Path.of(plugin.dataFolder.path, "plugins")
                Files.createDirectories(correctPluginPath)
                pluginPaths.clear()
                pluginPath(correctPluginPath)
            }
        }

        unloadDisabledExtensions()

        return bot
    }

    /**
     * Unloads disabled bot extensions
     */
    private suspend fun unloadDisabledExtensions() {
        for (extension in DiscordBotConstants.BOT_EXTENSIONS) {
            if (!extension.isEnabled()) kordexBot.await()?.unloadExtension(extension.extensionName())
        }
    }

    /**
     * Gives the Kord client used by the Discord bot
     *
     * @return The Kord client or null, if no Discord bot exists
     */
    suspend fun kordClient(): Kord? = kordexBot.await()?.getKoin()?.get()

    /**
     * Checks if the bot has yet been built
     *
     * @return Whether the bot was built
     */
    suspend fun isBuilt(): Boolean = kordexBot.await() != null
}
