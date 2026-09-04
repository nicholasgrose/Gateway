package xyz.rose.gateway.core.capability.chat.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.chat.Style

/**
 * A segment that represents a URL.
 *
 * @property displayText The text to display.
 * @property url The URL.
 * @property style The style of the segment.
 * @property fallbackText The fallback text to display if the URL is not supported.
 */
@Serializable
data class UrlSegment(
    val displayText: String,
    val url: String,
    override val style: Style = Style.NONE,
    override val fallbackText: String = url,
) : Segment
