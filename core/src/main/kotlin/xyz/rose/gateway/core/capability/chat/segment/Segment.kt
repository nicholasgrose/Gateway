package xyz.rose.gateway.core.capability.chat.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.chat.Style

/**
 * A segment of a message.
 *
 * @property fallbackText The fallback text to display if the segment is not supported.
 * @property style The style of the segment.
 */
@Serializable
sealed interface Segment {
    val fallbackText: String
    val style: Style
}

