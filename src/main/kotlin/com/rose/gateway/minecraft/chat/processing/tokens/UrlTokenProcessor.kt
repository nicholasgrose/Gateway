package com.rose.gateway.minecraft.chat.processing.tokens

import com.rose.gateway.minecraft.chat.processing.tokens.result.TokenProcessingResult
import com.rose.gateway.minecraft.component.component
import com.rose.gateway.minecraft.component.openUrlOnClick
import com.rose.gateway.minecraft.component.showTextOnHover
import com.rose.gateway.minecraft.component.underlined
import com.rose.gateway.shared.parsing.TokenProcessor
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import org.intellij.lang.annotations.Language

/**
 * Defines and processes a URL
 *
 * @constructor Create a URL token processor
 */
class UrlTokenProcessor : TokenProcessor<TokenProcessingResult, Unit> {
    override fun tokenType(): LixyTokenType {
        return ChatComponent.URL
    }

    @Language("RegExp")
    override fun regexPattern(): String {
        return "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    }

    override suspend fun process(token: LixyToken, additionalData: Unit): TokenProcessingResult {
        val url = token.string
        val component = url.component().underlined()
            .showTextOnHover("Click to open url".component())
            .openUrlOnClick(url)

        return TokenProcessingResult(component, url)
    }
}
