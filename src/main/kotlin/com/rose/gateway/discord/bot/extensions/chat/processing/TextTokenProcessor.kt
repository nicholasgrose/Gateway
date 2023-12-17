package com.rose.gateway.discord.bot.extensions.chat.processing

import com.rose.gateway.minecraft.component.component
import com.rose.gateway.shared.parsing.TokenProcessor
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.tegral.niwen.lexer.Token
import guru.zoroark.tegral.niwen.lexer.TokenType
import net.kyori.adventure.text.Component
import org.intellij.lang.annotations.Language

/**
 * Token processor that defines a text token and its processing
 *
 * @constructor Create a text token processor
 */
class TextTokenProcessor : TokenProcessor<Component, MessageCreateEvent> {
    override fun tokenType(): TokenType = DiscordChatComponent.TEXT

    @Language("RegExp")
    override fun regexPattern(): String = ".[^<]*"

    override suspend fun process(
        token: Token,
        additionalData: MessageCreateEvent,
    ): Component {
        return token.string.component()
    }
}
