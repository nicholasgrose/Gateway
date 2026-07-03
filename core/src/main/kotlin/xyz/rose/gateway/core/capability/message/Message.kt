package xyz.rose.gateway.core.capability.message

import xyz.rose.gateway.core.Enrichable
import xyz.rose.gateway.core.EnricherMetadata
import xyz.rose.gateway.core.capability.message.segment.Segment
import java.net.URL

data class Message(
    val sender: Sender,
    val segments: List<Segment> = emptyList(),
    val attachments: List<URL> = emptyList()
)

data class Sender(
    val username: String,
    val nickname: String = username,
    val style: Style = Style.NONE,
    override val metadata: Map<String, EnricherMetadata> = emptyMap()
) : Enrichable
