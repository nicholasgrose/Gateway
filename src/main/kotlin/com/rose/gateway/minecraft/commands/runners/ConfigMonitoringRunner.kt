package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.framework.args.ConfigItemArg
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.italic
import com.rose.gateway.minecraft.component.item
import com.rose.gateway.minecraft.component.join
import com.rose.gateway.minecraft.component.joinNewLine
import com.rose.gateway.minecraft.component.joinSpace
import com.rose.gateway.minecraft.component.plus
import com.rose.gateway.minecraft.component.primaryComponent
import com.rose.gateway.minecraft.component.runCommandOnClick
import com.rose.gateway.minecraft.component.secondaryComponent
import com.rose.gateway.minecraft.component.showTextOnHover
import com.rose.gateway.minecraft.component.tertiaryComponent
import com.rose.gateway.minecraft.component.underlined
import com.rose.gateway.minecraft.component.warningComponent
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
    fun sendAllConfigurationHelp(context: CommandExecuteContext): Boolean {
        sendConfigListHelp(context.bukkit.sender, configStringMap.allStrings())

        return true
    }

    /**
     * Sends a command sender help for a list of config strings
     *
     * @param sender The sender to receive the help message
     * @param items The list of config strings to send help for
     */
    private fun sendConfigListHelp(
        sender: CommandSender,
        items: List<String>,
    ) {
        val configs =
            items.map { config ->
                "* ".component() + item(config)
            }

        sender.sendMessage(
            joinNewLine(
                "Available Configurations: ".primaryComponent(),
                joinNewLine(configs),
            ),
        )
    }

    /**
     * Command that reloads the plugin config from disk
     *
     * @param context The command context without any arguments
     * @return Whether the command succeeded
     */
    fun reloadConfig(context: CommandExecuteContext): Boolean {
        context.bukkit.sender.sendMessage("Loading configuration...")

        pluginCoroutineScope.launch {
            val loadSuccessful = config.reloadConfig()

            if (loadSuccessful) {
                context.bukkit.sender.sendMessage("New configuration loaded successfully. Bot restarted.")
            } else {
                context.bukkit.sender.sendMessage("Failed to load new configuration. Bot stopped for safety.")
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
    fun saveConfig(context: CommandExecuteContext): Boolean {
        pluginCoroutineScope.launch {
            config.saveConfig()
            context.bukkit.sender.sendMessage("Saved current configuration.")
        }

        return true
    }

    /**
     * Command that sends the command sender the current status of the plugin configuration
     *
     * @param sender The sender to receive the message
     * @return Whether the command succeeded
     */
    fun sendConfigurationStatus(context: CommandExecuteContext): Boolean {
        val configStatus = if (config.notLoaded()) "Not Loaded (Check logs to fix file and then reload)" else "Loaded"

        context.bukkit.sender.sendMessage(
            joinSpace(
                "Config Status:".primaryComponent(),
                configStatus.component(),
            ),
        )

        return true
    }

    /**
     * Sends help for a config item to the sender of a command
     *
     * @param context The command context with the config item to send help for
     * @return Whether the command succeeded
     */
    fun sendConfigurationHelp(context: CommandExecuteContext, configArg: ConfigItemArg): Boolean {
        val item = context.valueOf(configArg)

        context.bukkit.sender.sendMessage(itemHelpMessage(item))

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
            "Configuration Help:".primaryComponent(),
            "Name: ".primaryComponent() + item.path.tertiaryComponent().italic(),
            join(
                "Type: ".primaryComponent(),
                item.type.simpleName.component(),
                (if (item.type.isMarkedNullable) "?" else "").warningComponent(),
            ),
            "Current Value: ".primaryComponent() + item.value.toString().component(),
            "Description: ".primaryComponent() + item.description.component(),
            "View All Configurations".secondaryComponent().underlined().italic()
                .showTextOnHover("Click to view all configurations.".component())
                .runCommandOnClick("/gateway config help"),
        )
    }
}
