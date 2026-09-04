package xyz.rose.gateway.core.capability.chat.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.chat.Style

/**
 * A segment that represents text.
 *
 * @property text The text to display.
 * @property style The style of the segment.
 * @property fallbackText The fallback text to display if the segment is not supported.
 */
@Serializable
data class TextSegment(
    val text: String,
    override val style: Style = Style.NONE,
    override val fallbackText: String = text
) : Segment
