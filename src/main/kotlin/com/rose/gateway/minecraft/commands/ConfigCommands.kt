package com.rose.gateway.minecraft.commands

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.framework.CommandContext
import com.rose.gateway.minecraft.commands.framework.TabCompletionContext
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.secondaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.tertiaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.warningColor
import com.uchuhimo.konf.Item
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender

class ConfigCommands(private val configuration: PluginConfiguration) {
    private val configStringMap = configuration.configurationStringMap

    fun setConfiguration(context: CommandContext): Boolean {
        if (informSenderOnInvalidConfiguration(context.sender, "set")) return true

        val path = context.commandArguments[0] as String
        val configSpec = configSpecFromArgs(path, context) ?: return true
        val newValue = context.commandArguments[1]
        setConfiguration(configSpec, newValue)

        context.sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text(path, configuration.tertiaryColor(), TextDecoration.ITALIC),
                Component.text("set to"),
                Component.text(newValue.toString(), configuration.secondaryColor(), TextDecoration.ITALIC),
                Component.text("successfully!")
            )
        )

        return true
    }

    private fun informSenderOnInvalidConfiguration(sender: CommandSender, attemptedAction: String): Boolean {
        return if (configuration.notLoaded()) {
            sender.sendMessage("Cannot $attemptedAction with unloaded configuration. Fix configuration file first.")
            true
        } else false
    }

    private fun configSpecFromArgs(path: String, context: CommandContext): Item<*>? {
        val configSpec = configuration.configurationStringMap.specificationFromString(path)

        return if (configSpec == null) {
            context.sender.sendMessage("Configuration not found. Please try again.")
            null
        } else configSpec
    }

    private fun <T> setConfiguration(item: Item<T>, newValue: Any?) {
        if (item.type.rawClass.isInstance(newValue)) {
            @Suppress("UNCHECKED_CAST")
            configuration[item] = newValue as T
        }
    }

    fun addConfiguration(context: CommandContext): Boolean {
        if (informSenderOnInvalidConfiguration(context.sender, "add")) return true
        val path = context.commandArguments[0] as String
        val configSpec = configSpecFromArgs(path, context) ?: return true

        val newValues = context.commandArguments.subList(1, context.commandArguments.size)

        addToConfiguration(configSpec, newValues)

        return true
    }

    private fun <T> addToConfiguration(item: Item<T>, newValues: List<Any?>) {
        val currentValues = configuration[item]

        if (currentValues !is List<*>) {
            return
        } else {
            configuration[item] = currentValuesWithNewValuesAppended(currentValues, newValues)
        }
    }

    private fun <T, N> currentValuesWithNewValuesAppended(currentValues: List<N>, newValues: List<Any?>): T {
        @Suppress("UNCHECKED_CAST")
        return (newValues as List<N>).toCollection(currentValues.toMutableList()) as T
    }

    fun removeConfiguration(context: CommandContext): Boolean {
        if (informSenderOnInvalidConfiguration(context.sender, "remove")) return true
        val path = context.commandArguments[0] as String
        val configSpec = configSpecFromArgs(path, context) ?: return true

        val newValues = context.commandArguments.subList(1, context.commandArguments.size)

        removeFromConfiguration(configSpec, newValues)

        return true
    }

    private fun <T> removeFromConfiguration(item: Item<T>, valuesToBeRemoved: List<Any?>) {
        val currentValues = configuration[item]

        if (currentValues !is List<*>) {
            return
        } else {
            configuration[item] = currentValuesWithNewValuesRemoved(currentValues, valuesToBeRemoved)
        }
    }

    private fun <T, N> currentValuesWithNewValuesRemoved(currentValues: List<N>, valuesToBeRemoved: List<Any?>): T {
        @Suppress("UNCHECKED_CAST")
        return currentValues.toMutableList().removeAll(valuesToBeRemoved as List<N>) as T
    }

    fun sendConfigurationHelp(sender: CommandSender, configSearchString: String): Boolean {
        val matchingConfigurations = configStringMap.matchingOrAllConfigurationStrings(configSearchString)

        if (matchingConfigurations.size == 1) {
            val path = matchingConfigurations[0]
            val matchingSpec = configStringMap.specificationFromString(path)!!

            sender.sendMessage(
                Component.join(
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
                        Component.text(matchingSpec.type.rawClass.simpleName),
                        Component.text(if (matchingSpec.nullable) "?" else "", configuration.warningColor())
                    ),
                    Component.join(
                        JoinConfiguration.noSeparators(),
                        Component.text("Current Value: ", configuration.primaryColor()),
                        Component.text(configuration[matchingSpec].toString())
                    ),
                    Component.join(
                        JoinConfiguration.noSeparators(),
                        Component.text("Description: ", configuration.primaryColor()),
                        Component.text(matchingSpec.description)
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
            )
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

    fun configCompletion(context: TabCompletionContext): List<String> {
        val currentConfigurationArgument = context.parsedArguments.last() as String

        return configStringMap.matchingOrAllConfigurationStrings(currentConfigurationArgument)
    }

    fun reloadConfig(context: CommandContext): Boolean {
        context.sender.sendMessage("Loading configuration.")

        val loadSuccessful = configuration.reloadConfiguration()

        if (loadSuccessful) {
            context.sender.sendMessage("New configuration loaded successfully.")
        } else {
            context.sender.sendMessage("Failed to load new configuration. Bot stopped for safety.")
        }

        return true
    }
}
