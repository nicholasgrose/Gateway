package xyz.rose.gateway.core.capability.chat

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.chat.segment.Segment

/**
 * A message sent by a sender.
 *
 * @property sender The sender of the message.
 * @property segments The segments of the message.
 * @property attachments The attachments of the message.
 */
@Serializable
data class Message(
    val sender: Sender,
    val segments: List<Segment> = emptyList(),
    val attachments: List<String> = emptyList()
)

/**
 * The sender of a message.
 *
 * @property username The username of the sender.
 * @property nickname The nickname of the sender.
 * @property style The style of the sender.
 */
@Serializable
data class Sender(
    val username: String,
    val nickname: String = username,
    val style: Style = Style.NONE
)
