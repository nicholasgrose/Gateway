package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.runner.NoArgs
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.join
import com.rose.gateway.minecraft.component.joinSpace
import com.rose.gateway.minecraft.component.primaryComponent
import com.rose.gateway.minecraft.component.runCommandOnClick
import com.rose.gateway.minecraft.component.showTextOnHover
import com.rose.gateway.minecraft.component.tertiaryComponent
import com.rose.gateway.minecraft.logging.Logger
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Commands that affect the Discord bot
 */
object BotCommands : KoinComponent {
    private val bot: DiscordBotController by inject()
    private val pluginScope: PluginCoroutineScope by inject()

    /**
     * Command that rebuilds the Discord bot
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun rebuild(context: CommandExecuteContext<NoArgs>): Boolean {
        sendAndLogMessage(
            context.bukkit.sender,
            join(
                "Discord bot will now rebuild. ".component(),
                "Check Status".tertiaryComponent()
                    .showTextOnHover("Click to view status".component())
                    .runCommandOnClick("/gateway bot status"),
            ),
        )

        pluginScope.launch {
            bot.rebuild()
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
        sendAndLogMessage(
            context.bukkit.sender,
            join(
                "Discord bot will now restart. ".component(),
                "Check Status".tertiaryComponent()
                    .showTextOnHover("Click to view status".component())
                    .runCommandOnClick("/gateway bot status"),
            ),
        )

        pluginScope.launch {
            bot.restart()
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
        sendAndLogMessage(context.bukkit.sender, "Stopping the Discord bot...".component())

        pluginScope.launch {
            bot.stop()
            sendAndLogMessage(context.bukkit.sender, "Discord bot stopped.".component())
        }

        return true
    }

    /**
     * Sends a sender a message and logs it in the server logs
     *
     * @param sender The sender to send the message to
     * @param message The message to send and log
     */
    private fun sendAndLogMessage(
        sender: CommandSender,
        message: Component,
    ) {
        Logger.info(PlainTextComponentSerializer.plainText().serialize(message))
        sender.sendMessage(message)
    }

    /**
     * Command that sends the command sender information about the bot's status
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun status(context: CommandExecuteContext<NoArgs>): Boolean {
        val status = bot.state.status
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
