package com.rose.gateway.minecraft.commands

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.minecraft.commands.framework.CommandContext
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

class BotCommands(val plugin: GatewayPlugin) {
    fun restartBot(context: CommandContext): Boolean {
        val restartStartMessage = "Restarting the Discord bot!"
        val restartFinishMessage = "Discord bot restarted!"
        sendAndLogMessage(context.sender, restartStartMessage)
        plugin.restartBot()
        sendAndLogMessage(context.sender, restartFinishMessage)

        return true
    }

    private fun sendAndLogMessage(sender: CommandSender, message: String) {
        Logger.log(message)
        sender.sendMessage(message)
    }

    fun botStatus(context: CommandContext): Boolean {
        val status = plugin.discordBot.botStatus
        context.sender.sendMessage(
            Component.join(
                Component.text(" "),
                Component.text("Bot Status:", plugin.configuration.primaryColor()),
                Component.text(status.status),
                Component.text(if (status.reason.isEmpty()) "" else "(${status.reason})")
            )
        )

        return true
    }
}
