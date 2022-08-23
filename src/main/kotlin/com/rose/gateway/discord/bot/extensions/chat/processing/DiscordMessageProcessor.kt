package com.rose.gateway.discord.bot.extensions.chat.processing

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.primaryColor
import com.rose.gateway.config.extensions.secondaryColor
import com.rose.gateway.config.extensions.tertiaryColor
import com.rose.gateway.minecraft.component.ComponentBuilder
import com.rose.gateway.shared.parsing.TextProcessor
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

/**
 * Provides functions for processing a Discord message and converting it to a Minecraft text [Component].
 *
 * @see Component
 */
object DiscordMessageProcessor : KoinComponent {
    private val config: PluginConfig by inject()

    /**
     * Creates a Minecraft text [Component] from a message creation event.
     *
     * @param event The event to convert to a [Component].
     * @return The text after processing.
     */
    suspend fun createMessage(event: MessageCreateEvent): Component {
        return Component.join(
            JoinConfiguration.noSeparators(),
            generateNameBlock(event),
            generateMessagePrefixBlock(event),
            generateMessageBlock(event),
            generateMessageSuffixBlock(event)
        )
    }

    /**
     * Creates the name block to show in Minecraft.
     *
     * @param event The event to convert into a name block.
     * @return The name block.
     */
    private fun generateNameBlock(event: MessageCreateEvent): ComponentLike {
        return Component.join(
            JoinConfiguration.noSeparators(),
            Component.text("<"),
            ComponentBuilder.discordMemberComponent(event.member!!),
            Component.text("> ")
        )
    }

    /**
     * Creates a prefix to the main Minecraft message.
     *
     * @param event The event to use as a source of data for the prefix.
     * @return The resultant prefix block.
     */
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

    /**
     * Creates a [Component] representing a referenced message.
     *
     * @param event The event to source the referenced message from.
     * @return The referenced message as a [Component] or null if none exists.
     */
    private suspend fun componentForReferencedMessage(event: MessageCreateEvent): ComponentLike? {
        val referencedMessage = event.message.referencedMessage ?: return null

        return replyReferenceComponent(referencedMessage, event)
    }

    /**
     * Creates a [Component] for the author referenced by a referenced message.
     *
     * @param referencedMessage The message being referenced.
     * @param event The event that must be used for additional data.
     * @return The author referenced by the message, if any.
     */
    private suspend fun replyReferenceComponent(referencedMessage: Message, event: MessageCreateEvent): Component? {
        val member = referencedMessageAuthor(referencedMessage, event) ?: return null

        return ComponentBuilder.atDiscordMemberComponent(member, config.secondaryColor())
            .decorate(TextDecoration.ITALIC)
    }

    /**
     * Determines which member of a Discord guild wrote a referenced message.
     *
     * @param referencedMessage The message being referenced.
     * @param event The event to source data from.
     * @return The member that sent the referenced message.
     */
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

    /**
     * Generates a [Component] containing the main message block for Minecraft.
     *
     * @param event The event ot pull data from.
     * @return The message for Minecraft.
     */
    private suspend fun generateMessageBlock(event: MessageCreateEvent): ComponentLike {
        return Component.join(
            JoinConfiguration.noSeparators(),
            textProcessor.parseText(event.message.content, event)
        )
    }

    /**
     * Generates the suffix [Component] for Minecraft.
     *
     * @param event The event to pull data from.
     * @return The message's suffix.
     */
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
