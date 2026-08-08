package xyz.rose.gateway.core.capability.message

import kotlinx.serialization.Serializable

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
