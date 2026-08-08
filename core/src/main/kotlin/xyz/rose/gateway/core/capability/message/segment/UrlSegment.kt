package xyz.rose.gateway.core.capability.message.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.message.Style

@Serializable
data class UrlSegment(
    val displayText: String,
    val url: String,
    override val style: Style = Style.NONE,
    override val fallbackText: String = url,
) : Segment
