package com.rose.gateway.minecraft.commands

import com.rose.gateway.configuration.Configurator
import com.rose.gateway.minecraft.commands.framework.CommandContext

object ConfigCommands {
    fun setConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config set")
        return true
    }

    fun addConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config add")
        return true
    }

    fun removeConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config remove")
        return true
    }

    fun configurationHelp(context: CommandContext): Boolean {
        val path = context.commandArguments[0] as String
        val configuration = Configurator.getConfigurationInformation(path)

        return if (configuration == null) {
            context.sender.sendMessage("ERROR: Invalid configuration path provided.")
            false
        } else {
            context.sender.sendMessage(
                """
                Name: $path
                Type: ${configuration.type.rawClass.simpleName}${if (configuration.nullable) "?" else ""}
                Current Value: ${Configurator.config[configuration]}
                Description: ${configuration.description}
                """.trimIndent()
            )
            true
        }
    }
}
