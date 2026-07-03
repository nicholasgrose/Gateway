package xyz.rose.gateway.core.capability.message.segment

import xyz.rose.gateway.core.EnricherMetadata
import xyz.rose.gateway.core.capability.message.Style

sealed interface MentionSegment : Segment {
    val prefix: String
    val name: String
}

data class UserMentionSegment(
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style,
    override val metadata: Map<String, EnricherMetadata> = emptyMap()
) : MentionSegment

data class RoleMentionSegment(
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style,
    override val metadata: Map<String, EnricherMetadata> = emptyMap()
) : MentionSegment

data class ChannelMentionSegment(
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style,
    override val metadata: Map<String, EnricherMetadata> = emptyMap()
) : MentionSegment
