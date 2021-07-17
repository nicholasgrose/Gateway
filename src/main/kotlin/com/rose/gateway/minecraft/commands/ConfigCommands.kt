package com.rose.gateway.minecraft.commands

import com.rose.gateway.configuration.Configurator
import com.rose.gateway.minecraft.commands.framework.CommandContext
import org.bukkit.command.CommandSender

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

    fun sendConfigurationHelp(sender: CommandSender, configSearchString: String): Boolean {
        var matchingConfigurations = Configurator.searchForMatchingConfigurations(configSearchString)
        if (matchingConfigurations.isEmpty()) matchingConfigurations = Configurator.searchForMatchingConfigurations("")

        if (matchingConfigurations.size == 1) {
            val path = matchingConfigurations[0]
            val configuration = Configurator.getConfigurationInformation(path)!!

            sender.sendMessage(
                """
                Name: $path
                Type: ${configuration.type.rawClass.simpleName}${if (configuration.nullable) "?" else ""}
                Current Value: ${Configurator.config[configuration]}
                Description: ${configuration.description}
                """.trimIndent()
            )
        } else {
            sender.sendMessage(
                """
                |Available configurations:
                |${matchingConfigurations.joinToString(prefix = "* ", separator = "\n* ")}
                """.trimMargin()
            )
        }

        return true
    }
}
