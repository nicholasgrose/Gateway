package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.discord.bot.BotStatus
import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.runner.NoArgs
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.joinSpace
import com.rose.gateway.minecraft.component.primaryComponent
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Commands that affect the Discord bot
 */
object BotCommands : KoinComponent {
    private val bot: DiscordBot by inject()
    private val pluginScope: PluginCoroutineScope by inject()

    /**
     * Command that rebuilds the Discord bot
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun rebuild(context: CommandExecuteContext<NoArgs>): Boolean {
        sendAndLogMessage(context.bukkit.sender, "Rebuilding the Discord bot. This may take a while...")

        pluginScope.launch {
            bot.rebuild()
            sendAndLogMessage(
                context.bukkit.sender, if (bot.botStatus == BotStatus.RUNNING) {
                    "Discord bot restarted."
                } else {
                    "Discord bot failed to restart. Check bot status for more info."
                }
            )
        }

        return true
    }

    /**
     * Command that restarts the Discord bot
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun restart(context: CommandExecuteContext<NoArgs>): Boolean {
        sendAndLogMessage(context.bukkit.sender, "Restarting the Discord bot...")

        pluginScope.launch {
            bot.restart()
            sendAndLogMessage(
                context.bukkit.sender, if (bot.botStatus == BotStatus.RUNNING) {
                    "Discord bot restarted."
                } else {
                    "Discord bot failed to restart. Check bot status for more info."
                }
            )
        }

        return true
    }

    /**
     * Stops the Discord bot
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun stop(context: CommandExecuteContext<NoArgs>): Boolean {
        sendAndLogMessage(context.bukkit.sender, "Stopping the Discord bot...")

        pluginScope.launch {
            bot.stop()
            sendAndLogMessage(context.bukkit.sender, "Discord bot stopped.")
        }

        return true
    }

    /**
     * Sends a sender a message abd logs it in the server logs
     *
     * @param sender The sender to send the message to
     * @param message The message to send and log
     */
    private fun sendAndLogMessage(sender: CommandSender, message: String) {
        Logger.info(message)
        sender.sendMessage(message)
    }

    /**
     * Command that sends the command sender information about the bot's status
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun status(context: CommandExecuteContext<NoArgs>): Boolean {
        val status = bot.botStatus
        context.bukkit.sender.sendMessage(
            joinSpace(
                "Bot Status:".primaryComponent(),
                status.status.component(),
                (if (status.reason.isEmpty()) "" else "(${status.reason})").component(),
            ),
        )

        return true
    }
}
