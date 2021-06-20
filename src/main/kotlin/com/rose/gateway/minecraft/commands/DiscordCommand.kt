package com.rose.gateway.minecraft.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DiscordCommand : CommandExecutor {
    companion object {
        const val COMMAND_NAME = "discord"
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage(
            """You can mention in Discord in the following ways:
            |* Include @USER or @"USER" to mention a user.
            |    ^ Matches closest user.
            |* Include @R=ROLE or @R="ROLE" to mention a role.
            |* Include @C=TEXT_CHANNEL to mention a text channel.
            |* Include @V=VOICE_CHANNEL to mention a voice channel.""".trimMargin()
        )

        return true
    }
}
