package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.runner.NoArgs
import com.rose.gateway.minecraft.component.ColorComponent
import com.rose.gateway.minecraft.component.italic
import com.rose.gateway.minecraft.component.item
import com.rose.gateway.minecraft.component.join
import com.rose.gateway.minecraft.component.joinNewLine
import com.rose.gateway.minecraft.component.joinSpace
import com.rose.gateway.minecraft.component.plus
import com.rose.gateway.minecraft.component.runCommandOnClick
import com.rose.gateway.minecraft.component.showTextOnHover
import com.rose.gateway.minecraft.component.underlined
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import com.rose.gateway.shared.reflection.simpleName
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Commands for monitoring the plugin config
 */
object ConfigMonitoringRunner : KoinComponent {
    private val config: PluginConfig by inject()
    private val configStringMap: ConfigStringMap by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    /**
     * Command that sends the command sender a help message for all configurations
     *
     * @param sender The sender to receive the help message
     * @return Whether the command succeeded
     */
    fun sendAllConfigurationHelp(sender: CommandSender): Boolean {
        sendConfigListHelp(sender, configStringMap.allStrings())

        return true
    }

    /**
     * Sends a command sender help for a list of config strings
     *
     * @param sender The sender to receive the help message
     * @param items The list of config strings to send help for
     */
    private fun sendConfigListHelp(sender: CommandSender, items: List<String>) {
        val configs = items.map { config ->
            Component.text("* ") + item(config)
        }

        sender.sendMessage(
            joinNewLine(
                ColorComponent.primary("Available Configurations: "),
                joinNewLine(configs)
            )
        )
    }

    /**
     * Command that reloads the plugin config from disk
     *
     * @param context The command context without any arguments
     * @return Whether the command succeeded
     */
    fun reloadConfig(context: CommandContext<NoArgs>): Boolean {
        context.sender.sendMessage("Loading configuration...")

        pluginCoroutineScope.launch {
            val loadSuccessful = config.reloadConfig()

            if (loadSuccessful) {
                context.sender.sendMessage("New configuration loaded successfully. Bot restarted.")
            } else {
                context.sender.sendMessage("Failed to load new configuration. Bot stopped for safety.")
            }
        }

        return true
    }

    /**
     * Command that saves the current plugin configuration to disk
     *
     * @param context The command context without any arguments
     * @return Whether the command succeeded
     */
    fun saveConfig(context: CommandContext<NoArgs>): Boolean {
        pluginCoroutineScope.launch {
            config.saveConfig()
            context.sender.sendMessage("Saved current configuration.")
        }

        return true
    }

    /**
     * Command that sends the command sender the current status of the plugin configuration
     *
     * @param sender The sender to receive the message
     * @return Whether the command succeeded
     */
    fun sendConfigurationStatus(sender: CommandSender): Boolean {
        val configStatus = if (config.notLoaded()) "Not Loaded (Check logs to fix file and then reload)" else "Loaded"

        sender.sendMessage(
            joinSpace(
                ColorComponent.primary("Config Status:"),
                Component.text(configStatus)
            )
        )

        return true
    }

    /**
     * Sends help for a config item to the sender of a command
     *
     * @param context The command context with the config item to send help for
     * @return Whether the command succeeded
     */
    fun sendConfigurationHelp(context: CommandContext<ConfigItemArgs>): Boolean {
        val item = context.args.item

        context.sender.sendMessage(itemHelpMessage(item))

        return true
    }

    /**
     * Creates a help message for a config item
     *
     * @param item The item to create the help message for
     * @return The created help message
     */
    private fun itemHelpMessage(item: Item<*>): Component {
        return joinNewLine(
            ColorComponent.primary("Configuration Help:"),
            ColorComponent.primary("Name: ") + ColorComponent.tertiary(item.path).italic(),
            join(
                ColorComponent.primary("Type: "),
                Component.text(item.type.simpleName),
                ColorComponent.warning(if (item.type.isMarkedNullable) "?" else "")
            ),
            ColorComponent.primary("Current Value: ") + Component.text(item.value.toString()),
            ColorComponent.primary("Description: ") + Component.text(item.description),
            ColorComponent.secondary("View All Configurations").underlined().italic()
                .showTextOnHover(Component.text("Click to view all configurations."))
                .runCommandOnClick("/gateway config help")
        )
    }
}
