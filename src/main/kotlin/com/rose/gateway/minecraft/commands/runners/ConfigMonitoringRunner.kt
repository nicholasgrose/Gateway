package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.primaryColor
import com.rose.gateway.config.extensions.secondaryColor
import com.rose.gateway.config.extensions.tertiaryColor
import com.rose.gateway.config.extensions.warningColor
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.runner.NoArguments
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import com.rose.gateway.shared.reflection.simpleName
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigMonitoringRunner : KoinComponent {
    private val config: PluginConfig by inject()
    private val configStringMap: ConfigStringMap by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    fun sendAllConfigurationHelp(sender: CommandSender): Boolean {
        sendConfigListHelp(sender, configStringMap.allStrings())

        return true
    }

    private fun sendConfigListHelp(sender: CommandSender, items: List<String>) {
        val configs = items.map { config ->
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("* "),
                Component.text(config, this.config.tertiaryColor(), TextDecoration.ITALIC)
                    .hoverEvent(
                        HoverEvent.showText(
                            Component.join(
                                JoinConfiguration.noSeparators(),
                                Component.text("Get help for "),
                                Component.text(config, this.config.tertiaryColor(), TextDecoration.ITALIC)
                            )
                        )
                    )
                    .clickEvent(ClickEvent.runCommand("/gateway config help $config"))
            )
        }

        sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.newline()),
                Component.text("Available Configurations: ", config.primaryColor()),
                Component.join(JoinConfiguration.separator(Component.newline()), configs)
            )
        )
    }

    private fun createIndividualSpecHelpMessage(item: Item<*>): Component {
        return Component.join(
            JoinConfiguration.separator(Component.newline()),
            Component.text("Configuration Help:", config.primaryColor()),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Name: ", config.primaryColor()),
                Component.text(item.path, config.tertiaryColor(), TextDecoration.ITALIC)
            ),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Type: ", config.primaryColor()),
                Component.text(item.type.simpleName),
                Component.text(if (item.type.isMarkedNullable) "?" else "", config.warningColor())
            ),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Current Value: ", config.primaryColor()),
                Component.text(item.value.toString())
            ),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Description: ", config.primaryColor()),
                Component.text(item.description)
            ),
            Component.text(
                "View All Configurations",
                config.secondaryColor(),
                TextDecoration.UNDERLINED,
                TextDecoration.ITALIC
            )
                .hoverEvent(HoverEvent.showText(Component.text("Click to view all configurations.")))
                .clickEvent(ClickEvent.runCommand("/gateway config help"))
        )
    }

    fun reloadConfig(context: CommandContext<NoArguments>): Boolean {
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

    fun saveConfig(context: CommandContext<NoArguments>): Boolean {
        pluginCoroutineScope.launch {
            config.saveConfig()
            context.sender.sendMessage("Saved current configuration.")
        }

        return true
    }

    fun sendConfigurationStatus(sender: CommandSender): Boolean {
        sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text("Config Status:", config.primaryColor()),
                Component.text(
                    if (config.notLoaded()) "Not Loaded (Check logs to fix file and then reload)" else "Loaded"
                )
            )
        )

        return true
    }

    fun sendConfigurationHelp(context: CommandContext<ConfigItemArgs>): Boolean {
        val item = context.arguments.item ?: return false

        context.sender.sendMessage(createIndividualSpecHelpMessage(item))

        return true
    }
}
