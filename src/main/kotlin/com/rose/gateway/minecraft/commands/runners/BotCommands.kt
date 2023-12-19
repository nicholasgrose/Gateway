package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.discord.bot.BotStatus
import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
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
    private val controller: DiscordBotController by inject()
    private val pluginScope: PluginCoroutineScope by inject()

    /**
     * Command that restarts the discord bot
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun restartBot(context: CommandExecuteContext): Boolean {
        sendAndLogMessage(context.bukkit.sender, "Restarting the Discord bot...")

        pluginScope.launch {
            controller.restart()

            if (controller.state.status == BotStatus.RUNNING) {
                sendAndLogMessage(context.bukkit.sender, "Discord bot restarted.")
            } else {
                sendAndLogMessage(
                    context.bukkit.sender,
                    "Discord bot failed to restart. Check bot status for more info.",
                )
            }
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
     * Command that sends the command sender information about the bot status
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun botStatus(context: CommandExecuteContext): Boolean {
        val status = controller.state.status
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
