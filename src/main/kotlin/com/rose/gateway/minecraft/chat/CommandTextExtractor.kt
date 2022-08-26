package com.rose.gateway.minecraft.chat

import com.rose.gateway.discord.text.discordBoldSafe

object CommandTextExtractor {
    private val displayProcessorMap = mapOf(
        "me" to CommandTextExtractor::processMe,
        "say" to CommandTextExtractor::processSay
    )

    fun processPlayerCommand(command: String, sender: String): String? {
        return extractCommandText(command, 1, sender)
    }

    fun processServerCommand(command: String): String? {
        return extractCommandText(command, 0, "Server")
    }

    private fun extractCommandText(fullCommand: String, commandStartIndex: Int, senderName: String): String? {
        val separatorIndex = fullCommand.indexOf(' ')

        if (separatorIndex >= 0) {
            val commandPrefix = fullCommand.substring(commandStartIndex, separatorIndex)
            val processor = displayProcessorMap[commandPrefix]

            if (processor != null) {
                return processor(fullCommand, senderName)
            }
        }

        return null
    }

    private fun processMe(command: String, sender: String): String {
        val commandEnd = command.indexOf(' ')
        val actionContent = command.substring(commandEnd + 1)

        return "*** ${sender.discordBoldSafe()}** $actionContent"
    }

    private fun processSay(command: String, sender: String): String {
        val commandEnd = command.indexOf(' ')
        val sayContent = command.substring(commandEnd + 1)

        return "**[${sender.discordBoldSafe()}]** $sayContent"
    }
}
