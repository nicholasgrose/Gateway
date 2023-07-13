package com.rose.gateway.minecraft.chat.processing.tokens.result

import com.rose.gateway.minecraft.component.warningComponent
import net.kyori.adventure.text.Component

/**
 * The result of processing a token from a Minecraft chat
 *
 * @property minecraftMessage The token as it will appear in Minecraft
 * @property discordMessage The token as it will appear in Discord
 * @constructor Create a token processing result with the given values
 */
data class TokenProcessingResult(
    val minecraftMessage: Component,
    val discordMessage: String,
) {
    companion object {
        /**
         * Creates a [TokenProcessingResult] representing a failed Discord mention
         *
         * @param text The text as it appeared in Minecraft
         * @return The [TokenProcessingResult] for the failure
         */
        fun error(text: String): TokenProcessingResult {
            return TokenProcessingResult(
                text.warningComponent(),
                text,
            )
        }
    }
}
