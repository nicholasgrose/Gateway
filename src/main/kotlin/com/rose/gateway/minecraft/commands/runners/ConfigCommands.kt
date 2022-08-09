package com.rose.gateway.minecraft.commands.runners

import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.secondaryColor
import com.rose.gateway.config.extensions.tertiaryColor
import com.rose.gateway.minecraft.commands.arguments.ConfigArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigListArgs
import com.rose.gateway.minecraft.commands.converters.StringArg
import com.rose.gateway.minecraft.commands.framework.data.CommandContext
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigCommands : KoinComponent {
    private val config: PluginConfig by inject()

    fun <T, A : ConfigArgs<T, A, R>, R : RunnerArg<T, A, R>> setConfig(context: CommandContext<A>): Boolean {
        val args = context.arguments
        val item = args.item
        val value = args.value

        if (item == null || value == null) return false

        item.set(value)
        sendConfirmation(context.sender, item, value)

        return true
    }

    private fun <T> sendConfirmation(sender: CommandSender, item: Item<T>, value: T) {
        sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text(item.path, config.tertiaryColor(), TextDecoration.ITALIC),
                Component.text("set to"),
                Component.text(value.toString(), config.secondaryColor(), TextDecoration.ITALIC),
                Component.text("successfully!")
            )
        )
    }

    private fun <T> sendAddConfirmation(sender: CommandSender, item: Item<T>, value: T) {
        sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text(value.toString(), config.secondaryColor(), TextDecoration.ITALIC),
                Component.text("added to"),
                Component.text(item.path, config.tertiaryColor(), TextDecoration.ITALIC),
                Component.text("successfully!")
            )
        )
    }

    private fun <T> sendRemoveConfirmation(sender: CommandSender, item: Item<T>, value: T) {
        sender.sendMessage(
            Component.join(
                JoinConfiguration.separator(Component.text(" ")),
                Component.text(value.toString(), config.secondaryColor(), TextDecoration.ITALIC),
                Component.text("removed from"),
                Component.text(item.path, config.tertiaryColor(), TextDecoration.ITALIC),
                Component.text("successfully!")
            )
        )
    }

    fun <T : Any, A : ConfigListArgs<T, A, R>, R : StringArg<A>> addConfiguration(context: CommandContext<A>): Boolean {
        val configItem = context.arguments.item
        val values = context.arguments.value

        if (configItem == null || values == null) return true

        addToConfiguration(configItem, values)
        sendAddConfirmation(context.sender, configItem, values)

        return true
    }

    private fun <T> addToConfiguration(item: Item<List<T>>, additionalValues: List<T>) {
        val currentValues = item.get()
        val newValues = currentValues + additionalValues

        item.set(newValues)
    }

    fun <T : Any, A : ConfigListArgs<T, A, R>, R : StringArg<A>> removeConfiguration(
        context: CommandContext<A>
    ): Boolean {
        val configItem = context.arguments.item
        val values = context.arguments.value

        if (configItem == null || values == null) return true

        removeFromConfiguration(configItem, values)
        sendRemoveConfirmation(context.sender, configItem, values)

        return true
    }

    private fun <T> removeFromConfiguration(item: Item<List<T>>, valuesToBeRemoved: List<T>) {
        val currentValues = item.get()
        val newValues = currentValues - valuesToBeRemoved.toSet()

        item.set(newValues)
    }
}
