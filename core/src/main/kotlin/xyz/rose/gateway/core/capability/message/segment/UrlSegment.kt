package xyz.rose.gateway.core.capability.message.segment

import xyz.rose.gateway.core.EnricherMetadata
import xyz.rose.gateway.core.capability.message.Style
import xyz.rose.gateway.core.platform.PlatformType
import java.net.URL

data class UrlSegment(
    val displayText: String,
    val url: URL,
    override val style: Style = Style.NONE,
    override val fallbackText: String = url.toURI().toASCIIString(),
    override val metadata: Map<PlatformType, Map<String, EnricherMetadata>> = emptyMap(),
) : Segment
