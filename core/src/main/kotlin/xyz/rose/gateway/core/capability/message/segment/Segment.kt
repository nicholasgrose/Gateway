package xyz.rose.gateway.core.capability.message.segment

import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.capability.message.Style

@Serializable
sealed interface Segment {
    val fallbackText: String
    val style: Style
}

