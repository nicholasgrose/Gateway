package xyz.rose.gateway.core.capability.chat

import kotlinx.serialization.Serializable

/**
 * The style of the text in a chat message.
 *
 * @property bold Whether the text is bold.
 * @property italic Whether the text is italic.
 * @property underline Whether the text is underlined.
 * @property strikethrough Whether the text is strikethrough.
 * @property obfuscated Whether the text is obfuscated.
 * @property color The color of the text.
 */
@Serializable
data class Style(
    val bold: Boolean,
    val italic: Boolean,
    val underline: Boolean,
    val strikethrough: Boolean,
    val obfuscated: Boolean,
    val color: Color,
) {
    companion object {
        /**
         * The default style of a chat message.
         */
        val NONE = Style(
            bold = false,
            italic = false,
            underline = false,
            strikethrough = false,
            obfuscated = false,
            Color.WHITE
        )
    }
}
