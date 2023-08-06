package com.rose.gateway.discord.bot

import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DiscordBotController : KoinComponent {
    private val pluginScope: PluginCoroutineScope by inject()

    val state = BotState()
    var bot = DiscordBot()

    /**
     * Starts the Discord bot
     */
    suspend fun start() {
        Logger.info("Starting Discord bot...")

        if (bot.isBuilt()) {
            Logger.warning("Could not start because no valid bot exists. Check bot status for error.")

            return
        }

        state.status = BotStatus.STARTING

        state.fillBotChannels()
        launchConcurrentBot()

        Logger.info("Discord bot ready!")
    }

    /**
     * Launches the bot in a new parallel task
     */
    private suspend fun launchConcurrentBot() {
        val startResult = bot.tryKordOperation("Running Discord Bot") {
            state.status = BotStatus.RUNNING

            Result.success(
                pluginScope.launch {
                    bot.kordexBot?.start()

                    state.status = BotStatus.STOPPED
                }
            )
        }

        state.botJob = startResult.getOrNull()
    }

    /**
     * Stops the Discord bot such that it can be restarted
     */
    suspend fun stop() {
        state.status = BotStatus.STOPPING

        bot.kordexBot?.stop()
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

        bot.kordexBot?.close()
        state.botJob?.join()
    }

    /**
     * Fully rebuilds the Discord bot and starts it
     */
    suspend fun rebuild() {
        close()

        if (bot.safelyBuildBot().isSuccess) {
            start()
        }
    }
}
