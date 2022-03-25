package com.rose.gateway.bot.extensions.chat.processing

import com.rose.gateway.shared.processing.TokenProcessor
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.intellij.lang.annotations.Language

class UrlTokenProcessor : TokenProcessor<Component, MessageCreateEvent> {
    override fun tokenType(): LixyTokenType {
        return DiscordChatComponent.URL
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    }

    override suspend fun process(token: LixyToken, additionalData: MessageCreateEvent): Component {
        val url = token.string

        return Component.text(url)
            .decorate(TextDecoration.UNDERLINED)
            .hoverEvent(HoverEvent.showText(Component.text("Click to open url")))
            .clickEvent(ClickEvent.openUrl(url))
    }
}
