package com.rose.gateway.bot.extensions.chat.processing

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.component.ComponentBuilder
import com.rose.gateway.shared.configurations.MinecraftConfiguration.primaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.secondaryColor
import com.rose.gateway.shared.configurations.MinecraftConfiguration.tertiaryColor
import com.rose.gateway.shared.processing.TextProcessor
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

class DiscordMessageProcessor(private val plugin: GatewayPlugin) {
    private val pluginConfiguration = plugin.configuration

    suspend fun createMessage(event: MessageCreateEvent): Component {
        return Component.join(
            JoinConfiguration.noSeparators(),
            generateNameBlock(event),
            generateMessagePrefixBlock(event),
            generateMessageBlock(event),
            generateMessageSuffixBlock(event)
        )
    }

    private fun generateNameBlock(event: MessageCreateEvent): ComponentLike {
        return Component.join(
            JoinConfiguration.noSeparators(),
            Component.text("<"),
            ComponentBuilder.discordMemberComponent(event.member!!, plugin),
            Component.text("> ")
        )
    }

    private suspend fun generateMessagePrefixBlock(event: MessageCreateEvent): ComponentLike {
        val referenceComponent = componentForReferencedMessage(event) ?: return Component.empty()

        return Component.join(
            JoinConfiguration.noSeparators(),
            Component.text("(Replying to ")
                .color(pluginConfiguration.primaryColor())
                .decorate(TextDecoration.ITALIC),
            referenceComponent,
            Component.text(") ")
                .color(pluginConfiguration.primaryColor())
                .decorate(TextDecoration.ITALIC)
        )
    }

    private suspend fun componentForReferencedMessage(event: MessageCreateEvent): ComponentLike? {
        val referencedMessage = event.message.referencedMessage ?: return null

        return replyReferenceComponent(referencedMessage, event)
    }

    private suspend fun replyReferenceComponent(referencedMessage: Message, event: MessageCreateEvent): Component? {
        val member = referencedMessageAuthor(referencedMessage, event) ?: return null

        return ComponentBuilder.atDiscordMemberComponent(member, plugin.configuration.secondaryColor(), plugin)
            .decorate(TextDecoration.ITALIC)
    }

    private suspend fun referencedMessageAuthor(referencedMessage: Message, event: MessageCreateEvent): Member? {
        val referencedAuthor = referencedMessage.author?.id ?: return null

        return event.getGuild()?.getMemberOrNull(referencedAuthor)
    }

    private val textProcessor = TextProcessor(
        listOf(
            UrlTokenProcessor(),
            UserMentionTokenProcessor(plugin),
            RoleMentionTokenProcessor(plugin),
            ChannelMentionTokenProcessor(plugin),
            TextTokenProcessor(),
        )
    )

    private suspend fun generateMessageBlock(event: MessageCreateEvent): ComponentLike {
        return Component.join(
            JoinConfiguration.noSeparators(),
            textProcessor.parseText(event.message.content, event)
        )
    }

    private fun generateMessageSuffixBlock(event: MessageCreateEvent): ComponentLike {
        if (event.message.attachments.isEmpty()) return Component.empty()

        return Component.text(" (Attachments: ")
            .decorate(TextDecoration.ITALIC)
            .color(pluginConfiguration.primaryColor())
            .append(
                Component.join(
                    JoinConfiguration.separator(
                        Component.text(", ")
                            .decorate(TextDecoration.ITALIC)
                            .color(pluginConfiguration.primaryColor())
                    ),
                    event.message.attachments.mapIndexed { index, attachment ->
                        Component.text("Attachment$index")
                            .decorate(TextDecoration.UNDERLINED)
                            .decorate(TextDecoration.ITALIC)
                            .color(pluginConfiguration.tertiaryColor())
                            .hoverEvent(HoverEvent.showText(Component.text("Open attachment link")))
                            .clickEvent(ClickEvent.openUrl(attachment.url))
                    }
                )
            )
            .append(
                Component.text(")")
                    .decorate(TextDecoration.ITALIC)
                    .color(pluginConfiguration.primaryColor())
            )
    }
}
