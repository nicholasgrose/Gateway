package xyz.rose.gateway.core.capability.chat.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.chat.Style

/**
 * A segment that represents a mention.
 *
 * @property prefix The prefix of the mention.
 * @property name The name of the mention.
 * @property fallbackText The fallback text to display if the segment is not supported.
 * @property style The style of the segment.
 */
@Serializable
sealed interface MentionSegment : Segment {
    val prefix: String
    val name: String
}

/**
 * A user mention segment.
 *
 * @property userId The ID of the user mentioned.
 */
@Serializable
data class UserMentionSegment(
    val userId: String,
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style
) : MentionSegment

/**
 * A role mention segment.
 *
 * @property roleId The ID of the role mentioned.
 */
@Serializable
data class RoleMentionSegment(
    val roleId: String,
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style
) : MentionSegment

/**
 * A channel mention segment.
 *
 * @property channelId The ID of the channel mentioned.
 */
@Serializable
data class ChannelMentionSegment(
    val channelId: String,
    override val prefix: String,
    override val name: String,
    override val fallbackText: String,
    override val style: Style
) : MentionSegment
