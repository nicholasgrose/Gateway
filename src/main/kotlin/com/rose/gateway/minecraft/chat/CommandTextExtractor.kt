package com.rose.gateway.minecraft.chat

import com.rose.gateway.discord.text.discordBoldSafe

private val commandDiscordFormatterMap = mapOf(
    "me" to ::formatMeCommand,
    "say" to ::formatSayCommand
)

/**
 * Formats a player's Minecraft command in a manner suitable for Discord
 *
 * @param commandMessage The full command-message sent by the player
 * @param sender The player that sent the command
 * @return The extracted text or null, if the command should not be displayed
 */
fun discordTextForPlayerCommand(commandMessage: String, sender: String): String? {
    // This has a start index of 1 because player-sent commands have a leading slash.
    return discordTextForCommand(commandMessage, 1, sender)
}

/**
 * Formats a server's Minecraft command in a manner suitable for Discord
 *
 * @param commandMessage The full command-message sent by the server
 * @return The Discord text or null, if the command should not be displayed
 */
fun discordTextForServerCommand(commandMessage: String): String? {
    // This has a start index of 0 because server-sent commands lack a leading slash.
    return discordTextForCommand(commandMessage, 0, "Server")
}

/**
 * Gives the text that should be posted in Discord to represent a command
 *
 * @param commandMessage The command-message sent
 * @param commandStartIndex The index at which the command starts
 * @param sender The command's sender
 * @return The Discord text or null, if the command should not be displayed
 */
private fun discordTextForCommand(commandMessage: String, commandStartIndex: Int, sender: String): String? {
    val separatorIndex = commandMessage.indexOf(' ')

    if (separatorIndex >= 0) {
        val commandPrefix = commandMessage.substring(commandStartIndex, separatorIndex)
        val processor = commandDiscordFormatterMap[commandPrefix]

        if (processor != null) {
            return processor(commandMessage, sender)
        }
    }

    return null
}

/**
 * Formats a /me command for Discord
 *
 * @param commandMessage The command-message sent
 * @param sender The command's sender
 * @return The text for Discord
 */
private fun formatMeCommand(commandMessage: String, sender: String): String {
    val commandEnd = commandMessage.indexOf(' ')
    val actionContent = commandMessage.substring(commandEnd + 1)

    return "*** ${sender.discordBoldSafe()}** $actionContent"
}

/**
 * Formats a /say command for Discord
 *
 * @param commandMessage The command-message sent
 * @param sender The command's sender
 * @return The text for Discord
 */
private fun formatSayCommand(commandMessage: String, sender: String): String {
    val commandEnd = commandMessage.indexOf(' ')
    val sayContent = commandMessage.substring(commandEnd + 1)

    return "**[${sender.discordBoldSafe()}]** $sayContent"
}
