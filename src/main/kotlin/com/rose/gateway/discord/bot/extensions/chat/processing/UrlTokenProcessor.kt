package com.rose.gateway.discord.bot.extensions.chat.processing

import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.openUrlOnClick
import com.rose.gateway.minecraft.component.showTextOnHover
import com.rose.gateway.minecraft.component.underlined
import com.rose.gateway.shared.parsing.TokenProcessor
import dev.kord.core.event.message.MessageCreateEvent
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import net.kyori.adventure.text.Component
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

        return url.component().underlined()
            .showTextOnHover("Click to open url".component())
            .openUrlOnClick(url)
    }
}
