package com.rose.gateway.minecraft.commands

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.minecraft.commands.framework.CommandContext
import com.rose.gateway.minecraft.commands.framework.TabCompletionContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.CommandSender

object ConfigCommands {
    fun setConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config set is not yet implemented")
        return true
    }

    fun addConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config add is not yet implemented")
        return true
    }

    fun removeConfiguration(context: CommandContext): Boolean {
        context.sender.sendMessage("config remove is not yet implemented")
        return true
    }

    private val PURPLE = TextColor.fromHexString("#F526ED")
    private val RED = TextColor.fromHexString("#EB4325")

    fun sendConfigurationHelp(sender: CommandSender, configSearchString: String): Boolean {
        val matchingConfigurations = Configurator.configurationTrie.searchOrGetAll(configSearchString)

        if (matchingConfigurations.size == 1) {
            val path = matchingConfigurations[0]
            val configuration = Configurator.getConfigurationInformation(path)!!

            sender.sendMessage(
                Component.join(
                    Component.newline(),
                    Component.join(
                        Component.empty(),
                        Component.text("Name: ", DiscordBot.getMentionColor()),
                        Component.text(path, PURPLE, TextDecoration.ITALIC)
                    ),
                    Component.join(
                        Component.empty(),
                        Component.text("Type: ", DiscordBot.getMentionColor()),
                        Component.text(configuration.type.rawClass.simpleName),
                        Component.text(if (configuration.nullable) "?" else "", RED)
                    ),
                    Component.join(
                        Component.empty(),
                        Component.text("Current Value: ", DiscordBot.getMentionColor()),
                        Component.text(Configurator.config[configuration].toString())
                    ),
                    Component.join(
                        Component.empty(),
                        Component.text("Description: ", DiscordBot.getMentionColor()),
                        Component.text(configuration.description)
                    ),
                    Component.text(
                        "View All Configurations",
                        DiscordBot.getMentionColor(),
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
                    Component.empty(),
                    Component.text("* "),
                    Component.text(
                        config,
                        DiscordBot.getMentionColor(),
                        TextDecoration.ITALIC
                    )
                        .hoverEvent(
                            HoverEvent.showText(
                                Component.join(
                                    Component.empty(),
                                    Component.text("Get help for "),
                                    Component.text(config, PURPLE, TextDecoration.ITALIC)
                                )
                            )
                        )
                        .clickEvent(ClickEvent.runCommand("/gateway config help $config"))
                )
            }

            sender.sendMessage(
                Component.join(
                    Component.newline(),
                    Component.text("Available Configurations: ", PURPLE),
                    Component.join(Component.newline(), configurations)
                )
            )
        }

        return true
    }

    fun configCompletion(context: TabCompletionContext): List<String> {
        val currentConfigurationArgument = context.parsedArguments[0] as String

        return Configurator.configurationTrie.searchOrGetAll(currentConfigurationArgument)
    }
}
