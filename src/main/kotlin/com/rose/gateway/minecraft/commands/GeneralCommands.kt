package com.rose.gateway.minecraft.commands

import com.rose.gateway.minecraft.commands.framework.data.CommandContext

object GeneralCommands {
    fun discordHelp(context: CommandContext): Boolean {
        context.sender.sendMessage(
            """
            You can mention in Discord in the following ways:
            Include @USER or @"USER" to mention a user.
              ^ Matches closest user.
            Include @R=ROLE or @R="ROLE" to mention a role.
            Include @C=TEXT_CHANNEL to mention a text channel.
            Include @V=VOICE_CHANNEL to mention a voice channel.
            """.trimIndent()
        )

        return true
    }
}
