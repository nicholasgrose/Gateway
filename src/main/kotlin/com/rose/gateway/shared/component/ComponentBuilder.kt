package com.rose.gateway.shared.component

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.secondaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.warningColor
import dev.kord.core.entity.Member
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

object ComponentBuilder {
    fun errorComponent(text: String, config: PluginConfiguration): Component {
        return Component.text(text)
            .color(config.warningColor())
    }

    fun primaryComponent(text: String, config: PluginConfiguration): Component {
        return Component.text(text)
            .color(config.primaryColor())
    }

    fun atDiscordMemberComponent(user: Member, userColor: TextColor, plugin: GatewayPlugin): Component {
        return Component.join(
            JoinConfiguration.noSeparators(),
            primaryComponent("@", plugin.configuration),
            discordMemberComponent(user, plugin)
                .color(userColor)
        )
    }

    fun discordMemberComponent(user: Member, plugin: GatewayPlugin): Component {
        return Component.text(user.displayName)
            .color(plugin.configuration.secondaryColor())
            .hoverEvent(
                HoverEvent.showText(
                    Component.join(
                        JoinConfiguration.noSeparators(),
                        Component.text("Username: "),
                        Component.text(user.username)
                            .color(plugin.configuration.primaryColor())
                            .decorate(TextDecoration.ITALIC)
                    )
                )
            )
    }
}
