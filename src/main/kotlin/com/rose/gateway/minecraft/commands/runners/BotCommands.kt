package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.primaryColor
import com.rose.gateway.discord.bot.BotStatus
import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.runner.NoArguments
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Commands that affect the Discord bot
 */
object BotCommands : KoinComponent {
    private val bot: DiscordBot by inject()
    private val config: PluginConfig by inject()
    private val pluginScope: PluginCoroutineScope by inject()

    /**
     * Command that restarts the discord bot
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun restartBot(context: CommandContext<NoArguments>): Boolean {
        sendAndLogMessage(context.sender, "Restarting the Discord bot...")

        pluginScope.launch {
            bot.restart()

            if (bot.botStatus == BotStatus.RUNNING) {
                sendAndLogMessage(context.sender, "Discord bot restarted.")
            } else {
                sendAndLogMessage(context.sender, "Discord bot failed to restart. Check bot status for more info.")
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
     * Command that sends the command sender information about the bot's status
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun botStatus(context: CommandContext<NoArguments>): Boolean {
        val status = bot.botStatus
        context.sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text("Bot Status:", config.primaryColor()),
                Component.text(status.status),
                Component.text(if (status.reason.isEmpty()) "" else "(${status.reason})")
            )
        )

        return true
    }
}
