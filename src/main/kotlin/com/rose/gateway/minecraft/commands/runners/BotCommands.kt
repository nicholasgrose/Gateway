package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.Logger
import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BotCommands : KoinComponent {
    val config: PluginConfiguration by inject()
    val bot: DiscordBot by inject()

    fun restartBot(context: CommandContext): Boolean {
        sendAndLogMessage(context.sender, "Restarting the Discord bot...")
        bot.restart()

        if (bot.isRunning()) {
            sendAndLogMessage(context.sender, "Discord bot restarted.")
        } else {
            sendAndLogMessage(context.sender, "Discord bot failed to restart. Check bot status for more info.")
        }

        return true
    }

    private fun sendAndLogMessage(sender: CommandSender, message: String) {
        Logger.logInfo(message)
        sender.sendMessage(message)
    }

    fun botStatus(context: CommandContext): Boolean {
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
