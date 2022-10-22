package com.rose.gateway.discord.bot.extensions.chat.processing

import com.rose.gateway.shared.parsing.TokenProcessor
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.intellij.lang.annotations.Language

/**
 * Token processor that defines a URL token and its processing
 *
 * @constructor Create a URL token processor
 */
class UrlTokenProcessor : TokenProcessor<Component, MessageCreateEvent> {
    override fun tokenType(): LixyTokenType = DiscordChatComponent.URL

    @Language("RegExp")
    override fun regexPattern(): String = "(https?|ftp|file)://[-a-zA-Z\\d+&@#/%?=~_|!:,.;]*[-a-zA-Z\\d+&@#/%=~_|]"

    override suspend fun process(token: LixyToken, additionalData: MessageCreateEvent): Component {
        val url = token.string

        return Component.text(url)
            .decorate(TextDecoration.UNDERLINED)
            .hoverEvent(HoverEvent.showText(Component.text("Click to open url")))
            .clickEvent(ClickEvent.openUrl(url))
    }
}
