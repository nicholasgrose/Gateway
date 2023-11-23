package com.rose.gateway.discord.bot

import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Manages the functionality and lifecycle of the Discord bot
 *
 * @constructor Create the Discord bot controller
 */
class DiscordBotController : KoinComponent {
    private val pluginScope: PluginCoroutineScope by inject()

    /**
     * Represents the current state of the bot
     */
    var state = BotState()

    /**
     * Represents a bot for Discord
     */
    var discordBot = DiscordBot()

    /**
     * Starts the Discord bot
     */
    suspend fun start() {
        Logger.info("Starting Discord bot...")

        state.status = BotStatus.STARTING
        launchConcurrentBot()

        Logger.info("Discord bot ready!")
    }

    /**
     * Launches the bot in a new parallel task
     */
    private suspend fun launchConcurrentBot() {
        state.botJob =
            pluginScope.launch {
                val runResult =
                    discordBot.tryKordOperation("Running Discord Bot") {
                        val bot =
                            discordBot.kordexBot.await()
                                ?: return@tryKordOperation Result.failure(
                                    Exception("No bot was built that could be started"),
                                )

                        state.status = BotStatus.RUNNING
                        bot.start()
                        state.status = BotStatus.STOPPED

                        Result.success(Unit)
                    }

                if (runResult.isFailure) {
                    state.status = BotStatus.STOPPED because runResult.exceptionOrNull()?.localizedMessage.toString()
                }
            }
    }

    /**
     * Stops the Discord bot such that it can be restarted
     */
    suspend fun stop() {
        state.status = BotStatus.STOPPING

        discordBot.kordexBot.await()?.stop()
        state.botJob?.join()
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
        state.status = BotStatus.STOPPING

        discordBot.kordexBot.await()?.close()
        state.botJob?.join()
    }

    /**
     * Fully rebuilds the Discord bot and starts it
     */
    suspend fun rebuild() {
        close()
        discordBot = DiscordBot()
        state = BotState()
        start()
    }
}
