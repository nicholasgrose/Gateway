package xyz.rose.gateway.core.capability.chat

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.GatewayMessage
import xyz.rose.gateway.core.capability.GatewayMessageData
import xyz.rose.gateway.core.capability.MessageMetadata
import xyz.rose.gateway.core.capability.chat.segment.Segment
import xyz.rose.gateway.core.platform.PlatformType
import xyz.rose.gateway.core.platform.PlatformUID
import xyz.rose.gateway.core.util.InstantSerializer
import xyz.rose.gateway.core.util.UuidSerializer
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * A message that represents a chat interaction.
 *
 * @property sender The sender of the message.
 * @property segments The segments of the message.
 */
@Serializable
data class ChatMessage(
    val sender: Sender,
    val segments: List<Segment> = emptyList(),
) : GatewayMessageData
