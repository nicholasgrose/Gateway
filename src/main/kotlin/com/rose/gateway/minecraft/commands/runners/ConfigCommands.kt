package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.configuration.Item
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.shared.configurations.MinecraftConfiguration.secondaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.tertiaryColor
import com.rose.gateway.shared.configurations.canBe
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender

class ConfigCommands(plugin: GatewayPlugin) {
    private val configuration = plugin.configuration

    private data class ConfigInfo(val path: String, val item: Item<*>)

    fun setConfiguration(context: CommandContext): Boolean {
        val configInfo = configItemFromContext(context, "set") ?: return true

        val newValue = context.commandArguments[1]
        setConfiguration(configInfo.item, newValue)

        context.sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text(configInfo.path, configuration.tertiaryColor(), TextDecoration.ITALIC),
                Component.text("set to"),
                Component.text(newValue.toString(), configuration.secondaryColor(), TextDecoration.ITALIC),
                Component.text("successfully!")
            )
        )

        return true
    }

    private fun configItemFromContext(context: CommandContext, attemptedAction: String): ConfigInfo? {
        if (informSenderOnInvalidConfiguration(context.sender, attemptedAction)) return null

        val path = context.commandArguments.first() as String
        val item = configuration[path]

        return if (item == null) {
            context.sender.sendMessage("Configuration not found. Please try again.")
            null
        } else ConfigInfo(path, item)
    }

    private fun informSenderOnInvalidConfiguration(sender: CommandSender, attemptedAction: String): Boolean {
        return if (configuration.notLoaded()) {
            sender.sendMessage("Cannot $attemptedAction with unloaded configuration. Fix configuration file first.")
            true
        } else false
    }

    private inline fun <T, reified U> setConfiguration(item: Item<T>, newValue: U) {
        if (U::class canBe item.typeClass()) {
            @Suppress("UNCHECKED_CAST")
            item.set(newValue as T)
        }
    }

    fun addConfiguration(context: CommandContext): Boolean {
        val configInfo = configItemFromContext(context, "add") ?: return true

        val newValues = context.commandArguments.subList(1, context.commandArguments.size)

        addToConfiguration(configInfo.item, newValues)

        return true
    }

    private fun <T> addToConfiguration(item: Item<T>, newValues: List<Any?>) {
        val currentValues = item.get()

        if (currentValues !is List<*>) {
            return
        } else {
            item.set(currentValuesWithNewValuesAppended(currentValues, newValues))
        }
    }

    private fun <T, N> currentValuesWithNewValuesAppended(currentValues: List<N>, newValues: List<Any?>): T {
        @Suppress("UNCHECKED_CAST")
        return (newValues as List<N>).toCollection(currentValues.toMutableList()) as T
    }

    fun removeConfiguration(context: CommandContext): Boolean {
        val configInfo = configItemFromContext(context, "remove") ?: return true

        val newValues = context.commandArguments.subList(1, context.commandArguments.size)

        removeFromConfiguration(configInfo.item, newValues)

        return true
    }

    private fun <T> removeFromConfiguration(item: Item<T>, valuesToBeRemoved: List<Any?>) {
        val currentValues = item.get()

        if (currentValues !is List<*>) {
            return
        } else {
            item.set(currentValuesWithNewValuesRemoved(currentValues, valuesToBeRemoved))
        }
    }

    private fun <T, N> currentValuesWithNewValuesRemoved(currentValues: List<N>, valuesToBeRemoved: List<Any?>): T {
        val newValues = currentValues.toMutableList()
        @Suppress("UNCHECKED_CAST")
        newValues.removeAll((valuesToBeRemoved as List<N>).toSet())

        @Suppress("UNCHECKED_CAST")
        return newValues as T
    }
}
