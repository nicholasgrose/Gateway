package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.shared.processing.ChatTokenProcessor
import guru.zoroark.lixy.LixyToken
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

class UrlTokenProcessor : ChatTokenProcessor<TokenProcessingResult> {
    override fun regexPattern(): String {
        return "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    }

    override suspend fun process(token: LixyToken): TokenProcessingResult {
        val url = token.string
        val component = Component.text(url)
            .decorate(TextDecoration.UNDERLINED)
            .hoverEvent(HoverEvent.showText(Component.text("Click to open url")))
            .clickEvent(ClickEvent.openUrl(url))

        return TokenProcessingResult(component, url)
    }
}
