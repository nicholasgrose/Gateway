package com.rose.gateway.discord.bot.extensions.chat.processing

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.primaryColor
import com.rose.gateway.config.extensions.secondaryColor
import com.rose.gateway.config.extensions.tertiaryColor
import com.rose.gateway.minecraft.component.ComponentBuilder
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DiscordMessageProcessor : KoinComponent {
    private val config: PluginConfig by inject()

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
            ComponentBuilder.discordMemberComponent(event.member!!),
            Component.text("> ")
        )
    }

    private suspend fun generateMessagePrefixBlock(event: MessageCreateEvent): ComponentLike {
        val referenceComponent = componentForReferencedMessage(event) ?: return Component.empty()

        return Component.join(
            JoinConfiguration.noSeparators(),
            Component.text("(Replying to ")
                .color(config.primaryColor())
                .decorate(TextDecoration.ITALIC),
            referenceComponent,
            Component.text(") ")
                .color(config.primaryColor())
                .decorate(TextDecoration.ITALIC)
        )
    }

    private suspend fun componentForReferencedMessage(event: MessageCreateEvent): ComponentLike? {
        val referencedMessage = event.message.referencedMessage ?: return null

        return replyReferenceComponent(referencedMessage, event)
    }

    private suspend fun replyReferenceComponent(referencedMessage: Message, event: MessageCreateEvent): Component? {
        val member = referencedMessageAuthor(referencedMessage, event) ?: return null

        return ComponentBuilder.atDiscordMemberComponent(member, config.secondaryColor())
            .decorate(TextDecoration.ITALIC)
    }

    private suspend fun referencedMessageAuthor(referencedMessage: Message, event: MessageCreateEvent): Member? {
        val referencedAuthor = referencedMessage.author?.id ?: return null

        return event.getGuild()?.getMemberOrNull(referencedAuthor)
    }

    private val textProcessor = TextProcessor(
        listOf(
            UrlTokenProcessor(),
            UserMentionTokenProcessor(),
            RoleMentionTokenProcessor(),
            ChannelMentionTokenProcessor(),
            TextTokenProcessor()
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
            .color(config.primaryColor())
            .append(
                Component.join(
                    JoinConfiguration.separator(
                        Component.text(", ")
                            .decorate(TextDecoration.ITALIC)
                            .color(config.primaryColor())
                    ),
                    event.message.attachments.mapIndexed { index, attachment ->
                        Component.text("Attachment$index")
                            .decorate(TextDecoration.UNDERLINED)
                            .decorate(TextDecoration.ITALIC)
                            .color(config.tertiaryColor())
                            .hoverEvent(HoverEvent.showText(Component.text("Open attachment link")))
                            .clickEvent(ClickEvent.openUrl(attachment.url))
                    }
                )
            )
            .append(
                Component.text(")")
                    .decorate(TextDecoration.ITALIC)
                    .color(config.primaryColor())
            )
    }
}
