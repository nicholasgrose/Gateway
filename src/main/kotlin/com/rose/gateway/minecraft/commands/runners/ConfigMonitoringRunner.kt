package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.ConfigurationStringMap
import com.rose.gateway.configuration.Item
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.runner.NoArguments
import com.rose.gateway.shared.configurations.primaryColor
import com.rose.gateway.shared.configurations.secondaryColor
import com.rose.gateway.shared.configurations.tertiaryColor
import com.rose.gateway.shared.configurations.warningColor
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigMonitoringRunner : KoinComponent {
    private val config: PluginConfiguration by inject()
    private val configStringMap: ConfigurationStringMap by inject()
    private val bot: DiscordBot by inject()

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
                Component.text(item.typeName()),
                Component.text(if (item.type().isMarkedNullable) "?" else "", config.warningColor())
            ),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Current Value: ", config.primaryColor()),
                Component.text(item.get().toString())
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

        val loadSuccessful = runBlocking {
            config.reloadConfiguration()
        }

        if (loadSuccessful) {
            runBlocking {
                bot.rebuild()
            }

            context.sender.sendMessage("New configuration loaded successfully. Bot restarted.")
        } else {
            context.sender.sendMessage("Failed to load new configuration. Bot stopped for safety.")
        }

        return true
    }

    fun saveConfig(context: CommandContext<NoArguments>): Boolean {
        config.saveConfiguration()
        context.sender.sendMessage("Saved current configuration.")

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
