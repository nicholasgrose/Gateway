package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.secondaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.tertiaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.warningColor
import com.uchuhimo.konf.Item
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender

class ConfigMonitoringRunner(private val plugin: GatewayPlugin) {
    private val configuration = plugin.configuration
    private val configStringMap = configuration.configurationStringMap

    fun sendConfigurationHelp(sender: CommandSender, configSearchString: String): Boolean {
        val matchingConfigurations = configStringMap.matchingOrAllConfigurationStrings(configSearchString)

        if (matchingConfigurations.size == 1) {
            val path = matchingConfigurations.first()
            val matchingSpec = configStringMap.specificationFromString(path)!!

            sender.sendMessage(createIndividualSpecHelpMessage(path, matchingSpec))
        } else {
            val configurations = matchingConfigurations.map { config ->
                Component.join(
                    JoinConfiguration.noSeparators(),
                    Component.text("* "),
                    Component.text(config, configuration.tertiaryColor(), TextDecoration.ITALIC)
                        .hoverEvent(
                            HoverEvent.showText(
                                Component.join(
                                    JoinConfiguration.noSeparators(),
                                    Component.text("Get help for "),
                                    Component.text(config, configuration.tertiaryColor(), TextDecoration.ITALIC)
                                )
                            )
                        )
                        .clickEvent(ClickEvent.runCommand("/gateway config help $config"))
                )
            }

            sender.sendMessage(
                Component.join(
                    JoinConfiguration.separator(Component.newline()),
                    Component.text("Available Configurations: ", configuration.primaryColor()),
                    Component.join(JoinConfiguration.separator(Component.newline()), configurations)
                )
            )
        }

        return true
    }

    private fun createIndividualSpecHelpMessage(path: String, spec: Item<*>): Component {
        return Component.join(
            JoinConfiguration.separator(Component.newline()),
            Component.text("Configuration Help:", configuration.primaryColor()),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Name: ", configuration.primaryColor()),
                Component.text(path, configuration.tertiaryColor(), TextDecoration.ITALIC)
            ),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Type: ", configuration.primaryColor()),
                Component.text(spec.type.rawClass.simpleName),
                Component.text(if (spec.nullable) "?" else "", configuration.warningColor())
            ),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Current Value: ", configuration.primaryColor()),
                Component.text(configuration[spec].toString())
            ),
            Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("Description: ", configuration.primaryColor()),
                Component.text(spec.description)
            ),
            Component.text(
                "View All Configurations",
                configuration.secondaryColor(),
                TextDecoration.UNDERLINED,
                TextDecoration.ITALIC
            )
                .hoverEvent(HoverEvent.showText(Component.text("Click to view all configurations.")))
                .clickEvent(ClickEvent.runCommand("/gateway config help"))
        )
    }

    fun reloadConfig(context: CommandContext): Boolean {
        context.sender.sendMessage("Loading configuration...")

        val loadSuccessful = configuration.reloadConfiguration()

        if (loadSuccessful) {
            runBlocking {
                plugin.restartBot()
            }

            context.sender.sendMessage("New configuration loaded successfully. Bot restarted.")
        } else {
            context.sender.sendMessage("Failed to load new configuration. Bot stopped for safety.")
        }

        return true
    }

    fun sendConfigurationStatus(sender: CommandSender): Boolean {
        sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text("Config Status:", configuration.primaryColor()),
                Component.text(
                    if (configuration.notLoaded()) "Not Loaded (Check logs to fix file and then reload)" else "Loaded"
                )
            )
        )

        return true
    }
}
