package com.rose.gateway.minecraft.chat.processing.tokens.result

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.shared.component.ComponentBuilder

class ResultBuilder(private val plugin: GatewayPlugin) {
    fun mentionResult(minecraftText: String, discordText: String): TokenProcessingResult {
        return TokenProcessingResult(
            ComponentBuilder.primaryComponent(minecraftText, plugin.configuration),
            discordText
        )
    }

    fun errorResult(text: String): TokenProcessingResult {
        return TokenProcessingResult(
            ComponentBuilder.errorComponent(text, plugin.configuration),
            text
        )
    }
}
