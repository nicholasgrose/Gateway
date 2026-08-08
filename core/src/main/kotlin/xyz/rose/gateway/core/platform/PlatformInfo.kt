package xyz.rose.gateway.core.platform

import kotlinx.serialization.Serializable

/**
 * Information about a Gateway platform.
 */
@Serializable
data class PlatformInfo(
    val name: String,
    val version: String
)
