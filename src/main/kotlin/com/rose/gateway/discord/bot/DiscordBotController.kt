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
    val state = BotState()

    /**
     * Represents a bot for Discord
     */
    val discordBot = DiscordBot()

    /**
     * Starts the Discord bot
     */
    suspend fun start() {
        Logger.info("Starting Discord bot...")

        if (discordBot.isBuilt()) {
            Logger.warning("Could not start because no valid bot exists. Check bot status for error.")

            return
        }

        state.status = BotStatus.STARTING
        launchConcurrentBot()

        Logger.info("Discord bot ready!")
    }

    /**
     * Launches the bot in a new parallel task
     */
    private suspend fun launchConcurrentBot() {
        val startResult = discordBot.tryKordOperation("Running Discord Bot") {
            state.status = BotStatus.RUNNING

            Result.success(
                pluginScope.launch {
                    discordBot.kordexBot?.start()

                    state.status = BotStatus.STOPPED
                },
            )
        }

        state.botJob = startResult.getOrNull()
    }

    /**
     * Stops the Discord bot such that it can be restarted
     */
    suspend fun stop() {
        state.status = BotStatus.STOPPING

        discordBot.kordexBot?.stop()
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

        discordBot.kordexBot?.close()
        state.botJob?.join()
    }

    /**
     * Fully rebuilds the Discord bot and starts it
     */
    suspend fun rebuild() {
        close()

        if (discordBot.safelyBuildBot().isSuccess) {
            start()
        }
    }
}
