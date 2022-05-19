package com.rose.gateway.minecraft.chat

import com.rose.gateway.shared.discord.discordBoldSafe

object DisplayCommandProcessor {
    private val displayProcessorMap = mapOf(
        "me" to DisplayCommandProcessor::processMe,
        "say" to DisplayCommandProcessor::processSay
    )

    fun processPlayerCommand(command: String, sender: String): String {
        val separatorIndex = command.indexOf(' ')

        if (separatorIndex >= 0) {
            val commandPrefix = command.substring(1, separatorIndex)
            val processor = displayProcessorMap[commandPrefix]

            if (processor != null) {
                return processor(command, sender)
            }
        }

        return ""
    }

    fun processServerCommand(command: String): String {
        val separatorIndex = command.indexOf(' ')

        if (separatorIndex >= 0) {
            val commandPrefix = command.substring(0, separatorIndex)
            val processor = displayProcessorMap[commandPrefix]

            if (processor != null) {
                return processor(command, "Server")
            }
        }

        return ""
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
