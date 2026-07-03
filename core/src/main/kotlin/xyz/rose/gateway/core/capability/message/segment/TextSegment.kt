package xyz.rose.gateway.core.capability.message.segment

import xyz.rose.gateway.core.EnricherMetadata
import xyz.rose.gateway.core.capability.message.Style

data class TextSegment(
    val text: String,
    override val style: Style = Style.NONE,
    override val fallbackText: String = text,
    override val metadata: Map<String, EnricherMetadata> = emptyMap(),
) : Segment
