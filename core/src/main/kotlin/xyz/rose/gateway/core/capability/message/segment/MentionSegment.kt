package xyz.rose.gateway.core.capability.message.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.message.Style

@Serializable
sealed interface MentionSegment : Segment {
    val prefix: String
    val name: String
}

@Serializable
data class UserMentionSegment(
    val userId: String,
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style
) : MentionSegment

@Serializable
data class RoleMentionSegment(
    val roleId: String,
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style
) : MentionSegment

@Serializable
data class ChannelMentionSegment(
    val channelId: String,
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style
) : MentionSegment
