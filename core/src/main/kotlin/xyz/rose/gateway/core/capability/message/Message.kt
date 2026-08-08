package xyz.rose.gateway.core.capability.message

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.message.segment.Segment
import java.net.URL

@Serializable
data class Message(
    val sender: Sender,
    val segments: List<Segment> = emptyList(),
    val attachments: List<String> = emptyList()
)

@Serializable
data class Sender(
    val username: String,
    val nickname: String = username,
    val style: Style = Style.NONE
)
