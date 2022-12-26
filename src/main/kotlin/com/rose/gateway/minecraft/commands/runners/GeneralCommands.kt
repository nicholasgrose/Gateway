package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.runner.NoArgs

/**
 * General plugin commands
 */
object GeneralCommands {
    /**
     * Command that sends the sender help for sending messages to Discord
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun discordHelp(context: CommandExecuteContext<NoArgs>): Boolean {
        context.bukkit.sender.sendMessage(
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
