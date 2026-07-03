package xyz.rose.gateway.core.capability.message.segment

import xyz.rose.gateway.core.Enrichable
import xyz.rose.gateway.core.capability.message.Style

sealed interface Segment : Enrichable {
    val fallbackText: String
    val style: Style
}

