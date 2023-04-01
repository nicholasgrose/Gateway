package com.rose.gateway.discord.bot.extensions.chat.processing

import com.kotlindiscord.kord.extensions.utils.getTopRole
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.secondaryColor
import com.rose.gateway.minecraft.component.atMember
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.italic
import com.rose.gateway.minecraft.component.join
import com.rose.gateway.minecraft.component.member
import com.rose.gateway.minecraft.component.openUrlOnClick
import com.rose.gateway.minecraft.component.primaryComponent
import com.rose.gateway.minecraft.component.showTextOnHover
import com.rose.gateway.minecraft.component.tertiaryComponent
import com.rose.gateway.minecraft.component.underlined
import com.rose.gateway.shared.parsing.TextProcessor
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.flow.toList
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextColor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides functions for processing a Discord message and converting it to a Minecraft text [Component]
 *
 * @see Component
 */
object DiscordMessageProcessor : KoinComponent {
    private val config: PluginConfig by inject()

    /**
     * Creates a Minecraft text [Component] from a message creation event
     *
     * @param event The event to convert to a [Component]
     * @return The text after processing
     */
    suspend fun createMessage(event: MessageCreateEvent): Component {
        return join(
            generateNameBlock(event),
            generateMessagePrefixBlock(event),
            generateMessageBlock(event),
            generateMessageSuffixBlock(event),
        )
    }

    /**
     * Creates the name block to show in Minecraft
     *
     * @param event The event to convert into a name block
     * @return The name block
     */
    private suspend fun generateNameBlock(event: MessageCreateEvent): Component {
        if (!(config.config.bot.extensions.chat.showRoleColor) || event.member!!.roles.toList().isEmpty()) {
            return join(
                "<".component(),
                member(event.member!!),
                "> ".component(),
            )
        }

        val roleColor = event.member!!.getTopRole()?.color?.let { TextColor.color(it.rgb) }

        return join(
            "<".component(),
            member(event.member!!).color(roleColor),
            "> ".component(),
        )
    }

    /**
     * Creates a prefix to the main Minecraft message
     *
     * @param event The event to use as a source of data for the prefix
     * @return The resultant prefix block
     */
    private suspend fun generateMessagePrefixBlock(event: MessageCreateEvent): Component {
        val referenceComponent = componentForReferencedMessage(event) ?: return Component.empty()

        return join(
            "(Replying to ".primaryComponent().italic(),
            referenceComponent,
            ") ".primaryComponent().italic(),
        )
    }

    /**
     * Creates a [Component] representing a referenced message
     *
     * @param event The event to source the referenced message from
     * @return The referenced message as a [Component] or null if none exists
     */
    private suspend fun componentForReferencedMessage(event: MessageCreateEvent): Component? {
        val referencedMessage = event.message.referencedMessage ?: return null

        return replyReferenceComponent(referencedMessage, event)
    }

    /**
     * Creates a [Component] for the author referenced by a referenced message
     *
     * @param referencedMessage The message being referenced
     * @param event The event that must be used for additional data
     * @return The author referenced by the message, if any
     */
    private suspend fun replyReferenceComponent(referencedMessage: Message, event: MessageCreateEvent): Component? {
        val member = referencedMessageAuthor(referencedMessage, event) ?: return null

        return atMember(member, config.secondaryColor()).italic()
    }

    /**
     * Determines which member of a Discord guild wrote a referenced message
     *
     * @param referencedMessage The message being referenced
     * @param event The event to source data from
     * @return The member that sent the referenced message
     */
    private suspend fun referencedMessageAuthor(referencedMessage: Message, event: MessageCreateEvent): Member? {
        val referencedAuthor = referencedMessage.author?.id ?: return null

        return event.getGuildOrNull()?.getMemberOrNull(referencedAuthor)
    }

    private val textProcessor = TextProcessor(
        listOf(
            UrlTokenProcessor(),
            UserMentionTokenProcessor(),
            RoleMentionTokenProcessor(),
            ChannelMentionTokenProcessor(),
            TextTokenProcessor(),
        ),
    )

    /**
     * Generates a [Component] containing the main message block for Minecraft
     *
     * @param event The event ot pull data from
     * @return The message for Minecraft
     */
    private suspend fun generateMessageBlock(event: MessageCreateEvent): Component {
        return join(textProcessor.parseText(event.message.content, event))
    }

    /**
     * Generates the suffix [Component] for Minecraft
     *
     * @param event The event to pull data from
     * @return The message's suffix
     */
    private fun generateMessageSuffixBlock(event: MessageCreateEvent): Component {
        if (event.message.attachments.isEmpty()) return Component.empty()

        return join(
            " (Attachments: ".primaryComponent().italic(),
            join(
                JoinConfiguration.separator(", ".primaryComponent().italic()),
                event.message.attachments.mapIndexed { index, attachment ->
                    "Attachment$index".tertiaryComponent().italic().underlined()
                        .showTextOnHover("Open attachment link".component()).openUrlOnClick(attachment.url)
                },
            ),
            ")".primaryComponent().italic(),
        )
    }
}
