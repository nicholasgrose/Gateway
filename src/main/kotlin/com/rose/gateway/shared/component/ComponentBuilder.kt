package com.rose.gateway.shared.component

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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ComponentBuilder : KoinComponent {
    val config: PluginConfiguration by inject()

    fun errorComponent(text: String): Component {
        return Component.text(text)
            .color(config.warningColor())
    }

    fun primaryComponent(text: String): Component {
        return Component.text(text)
            .color(config.primaryColor())
    }

    fun atDiscordMemberComponent(user: Member, userColor: TextColor): Component {
        return Component.join(
            JoinConfiguration.noSeparators(),
            primaryComponent("@"),
            discordMemberComponent(user)
                .color(userColor)
        )
    }

    fun discordMemberComponent(user: Member): Component {
        return Component.text(user.displayName)
            .color(config.secondaryColor())
            .hoverEvent(
                HoverEvent.showText(
                    Component.join(
                        JoinConfiguration.noSeparators(),
                        Component.text("Username: "),
                        Component.text(user.username)
                            .color(config.primaryColor())
                            .decorate(TextDecoration.ITALIC)
                    )
                )
            )
    }
}
