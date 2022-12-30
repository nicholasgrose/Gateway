package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.minecraft.commands.framework.args.NoArgs
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.joinNewLine
import com.rose.gateway.minecraft.component.joinSpace
import com.rose.gateway.minecraft.component.plus
import com.rose.gateway.minecraft.component.primaryComponent
import com.rose.gateway.minecraft.component.secondaryComponent
import com.rose.gateway.minecraft.component.showTextOnHover

/**
 * General plugin commands
 */
object DiscordCommands {
    private val USER_HELP_MESSAGE = "Mention the closest matching Discord user".component()
    private val USER_QUOTE_HELP_MESSAGE = USER_HELP_MESSAGE + " and allow spaces in their name".component()

    private val ROLE_HELP_MESSAGE = "Mention a Discord role".component()
    private val ROLE_QUOTE_HELP_MESSAGE = ROLE_HELP_MESSAGE + " and allow spaces in its name".component()

    private val TEXT_CHANNEL_HELP_MESSAGE = "Mention a Discord text channel".component()
    private val VOICE_CHANNEL_HELP_MESSAGE = "Mention a Discord voice channel".component()

    /**
     * Sends the sender help for chat messages in Discord
     *
     * @param context A command context without arguments
     * @return Whether the command succeeded
     */
    fun help(context: CommandExecuteContext<NoArgs>): Boolean {
        context.bukkit.sender.sendMessage(
            joinNewLine(
                "Discord mentions:".primaryComponent(),
                joinSpace(
                    "@USER".secondaryComponent().showTextOnHover(USER_HELP_MESSAGE),
                    "or".component(),
                    "@\"USER\"".secondaryComponent().showTextOnHover(USER_QUOTE_HELP_MESSAGE),
                ),
                joinSpace(
                    "@R=ROLE".secondaryComponent().showTextOnHover(ROLE_HELP_MESSAGE),
                    "or".component(),
                    "@R=\"ROLE\"".secondaryComponent().showTextOnHover(ROLE_QUOTE_HELP_MESSAGE),
                ),
                "@C=TEXT_CHANNEL".secondaryComponent().showTextOnHover(TEXT_CHANNEL_HELP_MESSAGE),
                "@V=VOICE_CHANNEL".secondaryComponent().showTextOnHover(VOICE_CHANNEL_HELP_MESSAGE),
            ),
        )

        return true
    }
}
