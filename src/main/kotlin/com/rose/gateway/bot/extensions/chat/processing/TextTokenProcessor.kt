package com.rose.gateway.bot.extensions.chat.processing

import com.rose.gateway.shared.processing.TokenProcessor
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import org.intellij.lang.annotations.Language

class TextTokenProcessor : TokenProcessor<Component, MessageCreateEvent> {
    override fun tokenType(): LixyTokenType {
        return DiscordChatComponent.TEXT
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return ".[^<]*"
    }

    override suspend fun process(token: LixyToken, additionalData: MessageCreateEvent): Component {
        return Component.text(token.string)
    }
}
