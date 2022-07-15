package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.configuration.Item
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.arguments.ConfigValueArgs
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.shared.configurations.canBe
import com.rose.gateway.shared.configurations.secondaryColor
import com.rose.gateway.shared.configurations.tertiaryColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextDecoration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigCommands : KoinComponent {
    private val config: PluginConfiguration by inject()

    fun setConfiguration(context: CommandContext<ConfigValueArgs>): Boolean {
        val args = context.arguments
        val configItem = args.configItem!!

        setConfiguration(configItem, args.configItem)

        context.sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text(configItem.path, config.tertiaryColor(), TextDecoration.ITALIC),
                Component.text("set to"),
                Component.text(args.configValue.toString(), config.secondaryColor(), TextDecoration.ITALIC),
                Component.text("successfully!")
            )
        )

        return true
    }

    private inline fun <T, reified U> setConfiguration(item: Item<T>, newValue: U) {
        if (U::class canBe item.typeClass()) {
            @Suppress("UNCHECKED_CAST")
            item.set(newValue as T)
        }
    }

    fun addConfiguration(context: CommandContext<ConfigValueArgs>): Boolean {
        val configItem = context.arguments.configItem ?: return true
        val values = context.arguments.configValue as List<*>

        addToConfiguration(configItem, values)

        return true
    }

    private fun <T> addToConfiguration(item: Item<T>, newValues: List<*>) {
        val currentValues = item.get()

        if (currentValues !is List<*>) {
            return
        } else {
            item.set(currentValuesWithNewValuesAppended(currentValues, newValues))
        }
    }

    private fun <T, N> currentValuesWithNewValuesAppended(currentValues: List<N>, newValues: List<*>): T {
        @Suppress("UNCHECKED_CAST")
        return (newValues as List<N>).toCollection(currentValues.toMutableList()) as T
    }

    fun removeConfiguration(context: CommandContext<ConfigValueArgs>): Boolean {
        val configItem = context.arguments.configItem ?: return true
        val values = context.arguments.configValue as List<*>

        removeFromConfiguration(configItem, values)

        return true
    }

    private fun <T> removeFromConfiguration(item: Item<T>, valuesToBeRemoved: List<*>) {
        val currentValues = item.get()

        if (currentValues !is List<*>) {
            return
        } else {
            item.set(currentValuesWithNewValuesRemoved(currentValues, valuesToBeRemoved))
        }
    }

    private fun <T, N> currentValuesWithNewValuesRemoved(currentValues: List<N>, valuesToBeRemoved: List<*>): T {
        val mutableCurrentValues = currentValues.toMutableList()

        @Suppress("UNCHECKED_CAST")
        mutableCurrentValues.removeAll((valuesToBeRemoved as List<N>).toSet())

        @Suppress("UNCHECKED_CAST")
        return mutableCurrentValues as T
    }
}
