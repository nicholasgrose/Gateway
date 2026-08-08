package xyz.rose.gateway.core.capability.message.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.message.Style

@Serializable
data class TextSegment(
    val text: String,
    override val style: Style = Style.NONE,
    override val fallbackText: String = text
) : Segment
